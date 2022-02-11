package com.personal.springframework.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.personal.springframework.model.core.BaseEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: springframework
 * @description: 用户
 * @author: 安少军
 * @create: 2021-12-30 10:36
 **/
@Data
public class User extends BaseEntity {
    private String userName;
    private String passWord;
    private String nickName;
    private String mail;
    private String tel;
    private Agency agency;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date lastLoginTime;
    private String lastLoginIp;
    private String portrait;
    private List<Role> roleList = new ArrayList<>();
    private List<Menu> menuList = new ArrayList<>();
}
