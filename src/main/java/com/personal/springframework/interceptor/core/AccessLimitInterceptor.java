package com.personal.springframework.interceptor.core;

import com.alibaba.fastjson.JSONObject;
import com.personal.springframework.annotation.AccessLimit;
import com.personal.springframework.constant.Constant;
import com.personal.springframework.util.IPUtil;
import com.personal.springframework.util.RedisUtil;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @program: springframework
 * @description: 访问过滤器
 * @author: 安少军
 * @create: 2022-01-11 10:40
 **/
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;
    private final ExpiringMap<String, Object> cache = ExpiringMap.builder().variableExpiration().build();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (null == accessLimit) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();

            String ip = IPUtil.getRealIP(request);
            String key = request.getServletPath() + ":" + ip;

            Integer count = Constant.REDIS_ENABLED ? (Integer) redisUtil.get(key) : (Integer) cache.get(key);

            if (null == count || -1 == count) {
                if (Constant.REDIS_ENABLED) {
                    redisUtil.set(key, 1, seconds);
                } else {
                    cache.put(key, 1, seconds, TimeUnit.SECONDS);
                }
                return true;
            }

            if (count < maxCount) {
                count = count + 1;
                if (Constant.REDIS_ENABLED) {
                    redisUtil.set(key, count, 0);
                } else {
                    cache.put(key, count);
                }
                return true;
            }

            if (count >= maxCount) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                JSONObject result = new JSONObject();
                result.put("code", 9999);
                result.put("message", "操作过于频繁");
                Object obj = JSONObject.toJSON(result);
                response.getWriter().write(JSONObject.toJSONString(obj));
                return false;
            }
        }
        return true;
    }
}
