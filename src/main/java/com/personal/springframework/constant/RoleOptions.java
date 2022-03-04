package com.personal.springframework.constant;

/**
 * @program: springframework
 * @description: 角色
 * @author: 安少军
 * @create: 2022-01-10 15:39
 **/
public enum RoleOptions {
    ALLDATA("1", "所有数据"),
    OWNDATA("2", "本单位数据"),
    CHILD("3", "本单位及下级单位"),
    DEFAULT("default", "普通用户"),
    ADMIN("admin", "超级管理员");
    private final String id;
    private final String name;

    private RoleOptions(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
