package com.personal.springframework.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: personalhub
 * @description: respose返回
 * @author: 安少军
 * @create: 2021-12-21 11:47
 **/
public class ResponseResult {
    private int code;
    private String msg;
    private Map<String, Object> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public static ResponseResult error(int code, String msg) {
        return new ResponseResult(code, msg);
    }
    public static ResponseResult success(int code, String msg) {
        return new ResponseResult(code, msg);
    }

    public ResponseResult put(String key, Object value) {
        if (this.data != null) {
            data.put(key, value);
        }
        return this;
    }

    private ResponseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = new HashMap<>();
    }
}
