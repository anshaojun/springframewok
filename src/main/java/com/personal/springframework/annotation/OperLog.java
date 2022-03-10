package com.personal.springframework.annotation;

import com.personal.springframework.model.enums.OperModel;
import com.personal.springframework.model.enums.OperType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperLog {
    OperModel operModel(); // 操作模块

    OperType operType();  // 操作类型

    String operDesc() default "";  // 操作说明

}
