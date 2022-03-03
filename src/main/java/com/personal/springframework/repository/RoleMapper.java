package com.personal.springframework.repository;

import com.personal.springframework.model.Menu;
import com.personal.springframework.model.Role;
import com.personal.springframework.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:RoleMapper
 * @author: anshaojun
 * @time: 2021-05-18 16:42
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    int deleteMenuRole(@Param("menuId") String menuId);

    int deleteRoleMenu(@Param("roleId") String roleId);

    void deleteRoleUser(@Param("roleId") String roleId);

    void deleteUserRole(@Param("userId") String userId);

    int batchDeleteRoleMenu(@Param("menus") List<Menu> menus);

    void batchInsertRoleMenu(@Param("roleId") String roleId,@Param("menus") List<String> menus);

    void batchDeleteRoleUser(@Param("users") List<User> users);

    void batchInsertRoleUser(@Param("roleId") String roleId, @Param("users") List<String> users);

    void deleteAgencyRole(@Param("agencyId") String agencyId);
}
