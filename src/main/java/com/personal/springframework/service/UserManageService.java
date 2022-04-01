package com.personal.springframework.service;

import com.personal.springframework.annotation.OperLog;
import com.personal.springframework.exception.ServiceException;
import com.personal.springframework.model.User;
import com.personal.springframework.model.enums.OperModel;
import com.personal.springframework.model.enums.OperType;
import com.personal.springframework.repository.RoleMapper;
import com.personal.springframework.repository.UserMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @program: springframewok
 * @description: 用户管理
 * @author: 安少军
 * @create: 2022-03-01 11:35
 **/
@Service
@Transactional(readOnly = true)
public class UserManageService extends AbstractService<User, UserMapper> {

    @Resource
    RoleMapper roleMapper;

    @Override
    @Transactional(readOnly = false)
    @OperLog(operType = OperType.SAVE,operModel = OperModel.USER,operDesc = "保存用户")
    public void save(User user) {
        InputStream inputStream = null;
        try {
            if (user.isNew()) {
                if (user.getHead() == null || user.getHead().getSize() == 0) {
                    inputStream = this.getClass().getResourceAsStream("/static/images/defaultportrait.jpg");
                } else {
                    inputStream = new ByteArrayInputStream(user.getHead().getBytes());
                }
            } else {
                if (user.getHead() != null && user.getHead().getSize() != 0) {
                    inputStream = new ByteArrayInputStream(user.getHead().getBytes());
                }
            }
            if (inputStream != null) {
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                inputStream.close();
                user.setPortrait(Base64.getEncoder().encodeToString(bytes));
            }
            super.saveNotExists(user, "user_name", "userName", "用户名");
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("内部数据错误");
        }
    }

    @Override
    @Transactional(readOnly = false)
    @OperLog(operType = OperType.DELETE,operModel = OperModel.USER,operDesc = "删除用户，级联删除用户角色关联")
    public void delete(String id) {
        try {
            if (StringUtils.isBlank(id)) {
                throw new ServiceException("获取id为空");
            }
            //删除用户角色关联
            roleMapper.deleteUserRole(id);
            super.delete(id);
        } catch (ServiceException se) {
            se.printStackTrace();
            throw se;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("内部数据错误");
        }
    }

    @Transactional(readOnly = false)
    @OperLog(operType = OperType.BATCHDELETE,operModel = OperModel.USER,operDesc = "批量删除用户，级联删除用户角色关联")
    public void batchDelete(String[] ids) {
        try {
            for (int i = 0; i < ids.length; i++) {
                //删除用户角色关联
                roleMapper.deleteUserRole(ids[i]);
                super.delete(ids[i]);
            }
        } catch (ServiceException se) {
            se.printStackTrace();
            throw se;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("内部数据错误");
        }
    }

    public Object loadUser() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (User user : mapper.getByParam(new HashMap<>())) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("title", user.getNickName()+"【"+user.getUserName()+"】");
            map.put("spread", true);
            map.put("children", new ArrayList<>());
            result.add(map);
        }
        return result;
    }
}
