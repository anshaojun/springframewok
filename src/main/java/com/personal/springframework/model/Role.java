package com.personal.springframework.model;

import com.personal.springframework.model.core.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @program: springframework
 * @description: 角色
 * @author: 安少军
 * @create: 2021-12-30 10:37
 **/
@Data
public class Role extends BaseEntity {
    //角色名称
    @NotNull(message = "角色名称不能为空")
    @Size(min = 1,max = 20,message = "角色名称长度为1-20")
    private String roleName;
    //权限
    private String permission;
    //菜单
    private List<Menu> menuList;
    //用户
    private List<User> userList;
}
