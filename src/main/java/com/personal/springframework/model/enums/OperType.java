package com.personal.springframework.model.enums;

/**
 * @program: springframewok
 * @description: 操作类型
 * @author: 安少军
 * @create: 2022-03-10 14:30
 **/
public enum OperType {
    INSERT("新增"),
    DELETE("删除"),
    UPDATE("修改"),
    QUERY("查询");
    private String name;


    OperType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
