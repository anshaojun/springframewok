package com.personal.springframework.service;

import cn.hutool.core.collection.CollectionUtil;
import com.personal.springframework.annotation.OperLog;
import com.personal.springframework.constant.AgencyOptions;
import com.personal.springframework.exception.ServiceException;
import com.personal.springframework.model.Agency;
import com.personal.springframework.model.enums.OperModel;
import com.personal.springframework.model.enums.OperType;
import com.personal.springframework.repository.AgencyMapper;
import com.personal.springframework.repository.RoleMapper;
import com.personal.springframework.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: springframewok
 * @description: 单位维护
 * @author: 安少军
 * @create: 2022-03-03 16:28
 **/
@Service
public class AgencyManageService extends AbstractService<Agency, AgencyMapper> {

    @Resource
    RoleMapper roleMapper;

    public Object loadAgencyTree(String roleId,boolean checkbox) {
        List<Agency> agencys = mapper.loadAgencyTree(roleId);
        List<Map<String, Object>> result = new ArrayList<>();
        return run(agencys, result, checkbox);
    }

    private Object run(List<Agency> agencys, List<Map<String, Object>> result, boolean checkbox) {
        for (Agency agency : agencys) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", agency.getId());
            map.put("title", agency.getAgencyName());
            map.put("parentId", agency.getParentId());
            if(checkbox){
                Map<String, String> checked = new HashMap<>();
                checked.put("type", "0");
                checked.put("checked", agency.getChecked() ? "1" : "0");
                map.put("checkArr", new Object[]{checked});
            }
            List<Agency> child = agency.getChild();
            List<Map<String, Object>> children = new ArrayList<>();
            map.put("children", run(child, children,checkbox));
            result.add(map);
        }
        return result;
    }

    @OperLog(operType = OperType.SAVE, operModel = OperModel.AGENCY, operDesc = "保存单位（新增、修改）")
    @Transactional(readOnly = false)
    public void save(Agency agency) {
        try {
            //校验
            //存在父级，则更新父级的是否底级为否
            if (agency.getParent() != null && StringUtils.isNotBlank(agency.getParent().getId())) {
                Agency parent = getById(agency.getParent().getId());
                parent.setIsLeaf(AgencyOptions.NORLEAF.getId());
                super.save(parent);
            } else {
                Agency parent = new Agency();
                parent.setId("0");
                agency.setParent(parent);
                if (!agency.getMLevel().equals("1")) {
                    throw new ServiceException("当前单位无父级，单位等级错误");
                }
            }
            if (CollectionUtil.isEmpty(agency.getChild())) {
                if (agency.getIsLeaf().equals(AgencyOptions.NORLEAF)) {
                    throw new ServiceException("单位无子单位，必须为底级");
                }
            }
            super.save(agency);
        } catch (ServiceException se) {
            se.printStackTrace();
            throw se;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("内部数据错误");
        }
    }

    @Override
    @Transactional(readOnly = false)
    @OperLog(operType = OperType.DELETE, operModel = OperModel.AGENCY, operDesc = "删除单位，级联删除单位角色关联、子单位")
    public void delete(String id) {
        try {
            //删除，级联删除单位角色关联、子单位
            super.delete(id);
            mapper.deleteByParent(id);
            roleMapper.deleteAgencyRole(id);
        } catch (ServiceException se) {
            se.printStackTrace();
            throw se;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("内部数据错误");
        }
    }
}
