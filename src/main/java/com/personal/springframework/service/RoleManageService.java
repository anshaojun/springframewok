package com.personal.springframework.service;

import com.google.common.collect.Lists;
import com.personal.springframework.exception.ServiceException;
import com.personal.springframework.model.Menu;
import com.personal.springframework.model.Role;
import com.personal.springframework.model.User;
import com.personal.springframework.repository.RoleMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @program: springframework
 * @description: 角色管理
 * @author: 安少军
 * @create: 2022-02-07 10:41
 **/
@Service
@Transactional(readOnly = true)
public class RoleManageService extends BaseService<Role, RoleMapper> {
    @Resource
    RoleMapper roleMapper;

    @Transactional(readOnly = false)
    public void delete(Role role) {
        try {
            if (StringUtils.isBlank(role.getId())) {
                throw new ServiceException("获取id为空");
            }
            //删除角色菜单关联
            roleMapper.deleteRoleMenu(role.getId());
            //删除角色用户关联
            roleMapper.deleteRoleUser(role.getId());
            super.delete(role.getId());
        } catch (ServiceException se) {
            se.printStackTrace();
            throw se;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("内部数据错误");
        }
    }

    @Transactional(readOnly = false)
    public void connectRoleMenu(String roleId, String[] menus) {
        Role role = getById(roleId);
        if (role == null) {
            throw new ServiceException("未找到角色");
        }
        //先删除，再维护新关系
        mapper.deleteRoleMenu(roleId);
        if (menus != null && menus.length != 0) {
            Lists.partition(Arrays.asList(menus), 50).forEach(g -> {
                mapper.batchInsertRoleMenu(roleId, g);
            });
        }
    }

    public void connectRoleUser(String roleId, String[] users) {
        Role role = getById(roleId);
        if (role == null) {
            throw new ServiceException("未找到角色");
        }
        //先删除，再维护新关系
        mapper.deleteRoleUser(roleId);
        if (users != null && users.length != 0) {
            Lists.partition(Arrays.asList(users), 50).forEach(g -> {
                mapper.batchInsertRoleUser(roleId, g);
            });
        }
    }
    @Transactional(readOnly = false)
    public void connectRoleAgency(String roleId, String[] agencys) {
        Role role = getById(roleId);
        if (role == null) {
            throw new ServiceException("未找到角色");
        }
        //先删除，再维护新关系
        mapper.deleteRoleAgency(roleId);
        if (agencys != null && agencys.length != 0) {
            Lists.partition(Arrays.asList(agencys), 50).forEach(g -> {
                mapper.batchInsertRoleAgency(roleId, g);
            });
        }
    }
}
