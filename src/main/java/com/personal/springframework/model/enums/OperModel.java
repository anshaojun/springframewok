package com.personal.springframework.model.enums;

/**
 * @program: springframewok
 * @description: 操作类型
 * @author: 安少军
 * @create: 2022-03-10 14:30
 **/
public enum OperModel {
    LOGIN("登录"),
    MENU("菜单"),
    USER("用户"),
    PERMISSION("权限"),
    ROLE("角色"),
    AGENCY("单位");
    private String name;

    OperModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
