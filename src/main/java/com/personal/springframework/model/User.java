package com.personal.springframework.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.personal.springframework.model.core.BaseEntity;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotNull(message = "用户名不能为空")
    @Size(max = 30,min = 1,message = "用户名长度为1-30")
    private String userName;
    @NotNull(message = "密码不能为空")
    @Size(max = 33,min = 1,message = "密码长度为1-33")
    private String passWord;
    @NotNull(message = "昵称不能为空")
    @Size(max = 30,min = 1,message = "昵称长度为1-30")
    private String nickName;
    @NotNull(message = "邮箱不能为空")
    @Size(max = 30,min = 1,message = "邮箱长度为1-30")
    private String mail;
    @NotNull(message = "手机号不能为空")
    @Size(max = 30,min = 1,message = "手机号长度为1-30")
    private String tel;
    private Agency agency;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date lastLoginTime;
    private String lastLoginIp;
    private String portrait;
    private List<Role> roleList = new ArrayList<>();
    private List<Menu> menuList = new ArrayList<>();
    private MultipartFile head;
}
