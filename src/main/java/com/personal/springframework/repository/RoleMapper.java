package com.personal.springframework.repository;

import com.personal.springframework.model.Menu;
import com.personal.springframework.model.Role;
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

    int batchDeleteRoleMenu(@Param("menus") List<Menu> menus);

    void batchInsertRoleMenu(@Param("roleId") String roleId,@Param("menus") List<String> menus);
}
