package com.personal.springframework.constant;

import java.io.IOException;
import java.util.Properties;

public final class Constant {
    static Properties properties = new Properties();

    static {
        try {
            properties.load(Constant.class.getResourceAsStream("/application-redis.properties"));
            properties.load(Constant.class.getResourceAsStream("/common.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final boolean REDIS_ENABLED = Boolean.valueOf(properties.getProperty("spring.redis.enabled"));
    public static final boolean MULTIACCOUNT_LOGIN = Boolean.valueOf(properties.getProperty("MULTIACCOUNT_LOGIN"));
    public static final String WEB_STATICFILE = String.valueOf(properties.getProperty("WEB_STATICFILE"));

    public static final long GLOBALSESSIONTIMEOUT = Long.valueOf(properties.getProperty("GLOBALSESSION_TIMEOUT"));
}
