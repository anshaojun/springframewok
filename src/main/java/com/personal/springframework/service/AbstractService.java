package com.personal.springframework.service;


import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.personal.springframework.constant.RoleOptions;
import com.personal.springframework.exception.ServiceException;
import com.personal.springframework.model.Agency;
import com.personal.springframework.model.Role;
import com.personal.springframework.model.core.BaseEntity;
import com.personal.springframework.model.core.Page;
import com.personal.springframework.repository.AgencyMapper;
import com.personal.springframework.repository.AbstractMapper;
import com.personal.springframework.repository.RoleMapper;
import com.personal.springframework.util.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: springframework
 * @description: service
 * @author: 安少军
 * @create: 2021-12-29 11:50
 **/
public abstract class AbstractService<T extends BaseEntity, M extends AbstractMapper<T>>  {

    //使用@Autowired自动装配
    @Autowired(required = false)
    protected M mapper;

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AgencyMapper agencyMapper;

    /**
     * @return java.lang.String
     * @Author 安少军
     * @Description 单位数据权限过滤
     * @Date 15:10 2022/3/4
     * @Param [targetTableAlies, targetTableColumn]
     **/
    protected String getDataScope(String targetTableAlies, String targetTableColumn) {
        List<Role> roles = UserUtil.getLoginUser().getRoleList();
        StringBuffer sql = new StringBuffer();
        Set<String> agencies = new LinkedHashSet<>();
        for (Role r : roles) {
            Role role = roleMapper.getById(r.getId());
            //存在所有数据权限
            if (role.getPermission().equals(RoleOptions.ALLDATA.getId())) {
                return "";
            }
            if (role.getPermission().equals(RoleOptions.OWNDATA.getId())) {
                Agency agency = role.getAgency();
                if (agency != null) {
                    agencies.add(agency.getId());
                }
            }
            if (role.getPermission().equals(RoleOptions.CHILD.getId())) {
                Agency agency = role.getAgency();
                if (agency != null) {
                    List<Agency> childs = agencyMapper.getAgenciesByParent(agency.getId());
                    childs.forEach(c -> {
                        agencies.add(c.getId());
                    });
                }
            }
        }
        if (agencies.size() > 1000) {
            throw new ServiceException("当前用户权限冗余");
        }
        sql.append(" and " + targetTableAlies + "." + targetTableColumn + " in( ");
        int i = 1;
        for (String agencyId : agencies) {
            sql.append("'").append(agencyId).append("'");
            if (i != agencies.size()) {
                sql.append(",");
            }
            i++;
        }
        sql.append(" ) ");
        return sql.toString();
    }

    @Transactional(readOnly = true)
    public Page<T> findPage(T clazz) {
        List<T> result = mapper.getByPage(clazz);
        Page page = new Page();
        BeanUtils.copyProperties(clazz, page);
        page.setList(result);
        return page;
    }

    @Transactional(readOnly = true)
    public T getById(String id) {
        return mapper.getById(id);
    }


    @Transactional(readOnly = false)
    public void delete(String id) {
        mapper.delete(id);
    }

    @Transactional(readOnly = false)
    public void save(T clazz) {
        if (clazz.isNew()) {
            mapper.insert(clazz);
        } else {
            mapper.update(clazz);
        }
    }

    @Transactional(readOnly = false)
    public void saveNotExists(T clazz, String column, String property, String text) {
        Field[] fields = clazz.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            if (name.equals(property)) {
                try {
                    Object value = field.get(clazz);
                    List<T> exists = mapper.getByUniqueParam(column, value);
                    if (CollectionUtil.isNotEmpty(exists)) {
                        if (clazz.isNew()) {
                            throw new ServiceException(text + "重复");
                        } else {
                            boolean exist = exists.stream().filter(o -> o.getId().equals(clazz.getId())).findAny().isPresent();
                            if (!exist) {
                                throw new ServiceException(text + "重复");
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if (clazz.isNew()) {
            mapper.insert(clazz);
        } else {
            mapper.update(clazz);
        }
    }

    @Transactional(readOnly = false)
    public void batchDelete(List<T> deletes) {
        List<List<T>> groups = Lists.partition(deletes, 50);
        groups.forEach(l -> {
            mapper.batchDelete(l);
        });
    }

}
