package com.personal.springframework.aspect;

import com.alibaba.fastjson.JSON;
import com.personal.springframework.annotation.OperLog;
import com.personal.springframework.model.core.OperationLog;
import com.personal.springframework.model.enums.OperModel;
import com.personal.springframework.model.enums.OperType;
import com.personal.springframework.service.OperationLogService;
import com.personal.springframework.util.IPUtil;
import com.personal.springframework.util.UserUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: springframewok
 * @description: 操作日志aop
 * @author: 安少军
 * @create: 2022-03-10 14:16
 **/
@Aspect
@Component
public class OperLogAspect {

    @Resource
    OperationLogService operationLogService;


    @AfterReturning(value = "@annotation(operLog)",returning = "returns")
    public void saveOperLog(JoinPoint joinPoint, Object returns,OperLog operLog) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

        OperationLog operlog = new OperationLog();
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取操作
            OperLog opLog = method.getAnnotation(OperLog.class);
            if (opLog != null) {
                OperModel operModul = opLog.operModel();
                OperType operType = opLog.operType();
                String operDesc = opLog.operDesc();
                operlog.setOperModel(operModul.getName());
                operlog.setOperType(operType.getName());
                operlog.setOperDesc(operDesc);
            }
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;

            operlog.setOperMethod(methodName); // 请求方法
            // 请求的参数
            Map<String, String> rtnMap = converMap(request.getParameterMap());
            // 将参数所在的数组转换成json
            String params = JSON.toJSONString(rtnMap);

            operlog.setOperParam(params); // 请求参数
            operlog.setOperReturn(JSON.toJSONString(returns)); // 返回结果
            operlog.setOperUserId(UserUtil.getLoginUser().getId()); // 请求用户ID
            operlog.setOperUserName(UserUtil.getLoginUser().getUserName()); // 请求用户名称
            operlog.setOperIp(IPUtil.getRealIP(request)); // 请求IP
            operlog.setOperUri(request.getRequestURI()); // 请求URI
            operlog.setOperTime(new Date()); // 创建时间
            operationLogService.save(operlog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }
}
