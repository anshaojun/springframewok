package com.personal.springframework.exception;

import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

/**
 * @program: personalhub
 * @description: 自定义异常
 * @author: 安少军
 * @create: 2021-12-21 10:54
 **/
public class ServiceException extends RuntimeException {
    private String msg_format;
    private Object[] msg_params;

    public ServiceException(String msg) {
        super(msg);
        this.msg_format = msg;
    }

    public ServiceException(String msg, Object... msg_params) {
        this.msg_format = msg;
        this.msg_params = msg_params;
    }

    @Override
    public String getMessage() {
        if (StringUtils.isNotBlank(msg_format)) {
            if (msg_params != null && msg_params.length > 0) {
                return MessageFormat.format(msg_format, msg_params);
            }
        }
        return super.getMessage();
    }
}
