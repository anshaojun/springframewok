package com.personal.springframework.annotation;


import java.lang.annotation.*;

/**
 * @author anshaojun
 * @date 2022/5/19 0019 11:50
 * @description 使用当前用户单位作为数据的权限校验
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AgencyAuth {
    /**
     * 参数索引
     */
    int index() default 0;

    String targetTableAlies();

    String targetTableColumn();
}
