package com.personal.springframework.model.enums;

/**
 * @program: personalhub
 * @description: valid返回异常
 * @author: 安少军
 * @create: 2021-12-21 11:41
 **/
public enum BizCodeEnum {
    SUCCESS(200,"成功"),
    SERVICE_EXCEPTION(500,"业务逻辑错误"),
    INNER_EXCEPTION(500,"内部数据错误"),
    UNKNOW_EXCEPTION(10000, "系统未知异常"),
    VAILD_EXCEPTION(400, "参数格式校验失败"),

    UNKNOWNACCOUNT_EXCEPTION(500, "未知账户"),
    VALIDATION_EXCEPTION(500, "验证码已失效"),
    VALIDATION_ERROR_EXCEPTION(500, "验证码错误"),
    INCORRECTCREDENTIALS_EXCEPTION(500, "密码错误"),
    LOCKEDACCOUNT_EXCEPTION(500, "账户锁定"),
    EXCESSIVEATTEMPTS_EXCEPTION(500, "错误次数过多"),
    AUTHENTICATION_EXCEPTION(500, "用户名或密码错误"),
    SESSION_EXCEPTION(500, "登录已失效"),
    UNAUTHORIZED_EXCEPTION(304, "权限不足"),
    LOGIN_EXCEPTION(500, "登陆失败");

    private int code;
    private String msg;

    BizCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
