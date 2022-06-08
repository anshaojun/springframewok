package com.personal.springframework.controller.advice;

import com.personal.springframework.exception.ServiceException;
import com.personal.springframework.model.ResponseResult;
import com.personal.springframework.model.enums.BizCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @program: personalhub
 * @description: 捕获controller没有catch的异常，用于参数校验、业务异常等的封装
 * @author: 安少军
 * @create: 2021-12-21 11:14
 **/
@Slf4j
@ControllerAdvice(basePackages = "com.personal.springframework.controller")
public class GlobalExceptionControllerAdvice {

    //参数校验异常
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ResponseResult handleVaildException(BindException e) {
        BindingResult result = e.getBindingResult();
        StringBuffer sb = new StringBuffer();
        String msg = "";
        result.getFieldErrors().forEach(item -> {
            String message = item.getDefaultMessage();
            sb.append(message);
            sb.append(",");
        });
        if (sb.length() != 0) {
            msg = ":" + sb.substring(0, sb.lastIndexOf(","));
        }
        e.printStackTrace();
        return ResponseResult.error(BizCodeEnum.VAILD_EXCEPTION.getCode(), BizCodeEnum.VAILD_EXCEPTION.getMsg() + msg);
    }

    //自定义异常
    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    public ResponseResult handleException(ServiceException e) {
        e.printStackTrace();
        return ResponseResult.error(BizCodeEnum.SERVICE_EXCEPTION.getCode(), BizCodeEnum.SERVICE_EXCEPTION.getMsg() + ":" + e.getMessage());
    }

    //400参数缺失异常
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseResult handleException(MissingServletRequestParameterException e) {
        e.printStackTrace();
        return ResponseResult.error(BizCodeEnum.VAILD_EXCEPTION.getCode(), BizCodeEnum.VAILD_EXCEPTION.getMsg());
    }

    //shiro权限异常
    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseBody
    public ResponseResult handleException(UnauthorizedException e) {
        e.printStackTrace();
        return ResponseResult.error(BizCodeEnum.UNAUTHORIZED_EXCEPTION.getCode(), BizCodeEnum.UNAUTHORIZED_EXCEPTION.getMsg());
    }

    //SQL异常
    @ExceptionHandler(value = BadSqlGrammarException.class)
    @ResponseBody
    public ResponseResult handleException(BadSqlGrammarException e) {
        e.printStackTrace();
        return ResponseResult.error(BizCodeEnum.INNER_EXCEPTION.getCode(), BizCodeEnum.INNER_EXCEPTION.getMsg());
    }
    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    @ResponseBody
    public ResponseResult handleException(SQLIntegrityConstraintViolationException e) {
        e.printStackTrace();
        return ResponseResult.error(BizCodeEnum.INNER_EXCEPTION.getCode(), BizCodeEnum.INNER_EXCEPTION.getMsg());
    }

    //其他异常
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseResult handleException(Exception e) {
        e.printStackTrace();
        return ResponseResult.error(BizCodeEnum.UNKNOW_EXCEPTION.getCode(), BizCodeEnum.UNKNOW_EXCEPTION.getMsg());
    }

}
