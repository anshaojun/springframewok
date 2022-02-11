package com.personal.springframework.constant;

/**
 * @program: springframework
 * @description: 角色
 * @author: 安少军
 * @create: 2022-01-10 15:39
 **/
public enum MenuOptions {
    ONE_LEVEL("1", "一级菜单"),
    TWO_LEVEL("2", "二级菜单"),
    THREE_LEVEL("3", "三级菜单（按钮）"),
    MENU("0","菜单"),
    BUTTON("1","按钮"),
    LEAF("1","是"),
    NORLEAF("0","否");
    private final String id;
    private final String name;

    private MenuOptions(String id, String name) {
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
