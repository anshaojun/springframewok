package com.personal.springframework.model.enums;

/**
 * @program: personalhub
 * @description: valid返回异常
 * @author: 安少军
 * @create: 2021-12-21 11:41
 **/
public enum Permission {
    SUCCESS("1", "所有数据"),
    SERVICE_EXCEPTION("2", "本单位数据"),
    INNER_EXCEPTION("3", "指定单位数据");

    private String code;
    private String name;

    Permission(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
