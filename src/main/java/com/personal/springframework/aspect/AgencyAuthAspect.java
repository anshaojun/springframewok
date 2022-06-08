package com.personal.springframework.aspect;

import cn.hutool.core.lang.func.Func;
import com.personal.springframework.annotation.AgencyAuth;
import com.personal.springframework.model.core.BaseEntity;
import com.personal.springframework.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author anshaojun
 * @date 2022/5/19 0019 15:06
 * @description
 */
@Aspect
@Component
@Slf4j
public class AgencyAuthAspect {

    @Around("@annotation(agencyAuth)")
    public Object aroundRedisLock(ProceedingJoinPoint joinPoint, AgencyAuth agencyAuth) {
        try {
            Object[] objects = joinPoint.getArgs();
            if (null != objects) {
                Object param = objects[agencyAuth.index()];
                if (param instanceof BaseEntity) {
                    changObjectValue(param, agencyAuth.targetTableAlies(), agencyAuth.targetTableColumn());
                }
            }
            return joinPoint.proceed(objects);
        } catch (Throwable e) {
            log.error("目标方法执行异常,目标类:" + joinPoint.getTarget() + "方法：" + joinPoint.getSignature().getName(), e);
            throw new RuntimeException("系统繁忙，请稍后再试!");
        }
    }

    private void changObjectValue(Object obj, String targetTableAlies, String targetTableColumn) throws Exception {
        Class<?> resultClz = obj.getClass();
        Field[] fieldInfo = resultClz.getSuperclass().getDeclaredFields();
        for (Field field : fieldInfo) {
            if ("authSql".equals(field.getName())) {
                field.setAccessible(true);
                String authSql = " and exists(select 1 from tbl_core_role_user tru,tbl_core_role_agency tra" +
                        " where tru.role_id = tra.role_id " +
                        " and tru.user_id = '" + UserUtil.getLoginUser().getId() + "' " +
                        " and tra.agency_id = " + targetTableAlies + "." + targetTableColumn + " " +
                        ")";
                field.set(obj, authSql);
                break;
            }
        }
    }
}
