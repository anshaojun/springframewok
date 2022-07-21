package com.personal.springframework.model.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @program: springframewok
 * @description: 操作日志
 * @author: 安少军
 * @create: 2022-03-10 14:24
 **/
@Data
public class OperationLog extends BaseEntity{
    //操作模块
    private String operModel;
    //操作类型
    private String operType;
    //操作描述
    private String operDesc;
    //请求方法
    private String operMethod;
    //请求参数
    private String operParam;
    //返回结果
    private String operReturn;
    //用户id
    private String operUserId;
    //用户名
    private String operUserName;
    //操作ip
    private String operIp;
    //请求uri
    private String operUri;
    //操作时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operTime;

}
