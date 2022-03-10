package com.personal.springframework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(exclude = {RedisAutoConfiguration.class,RedisRepositoriesAutoConfiguration.class})
@EnableAsync
@EnableKafka
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableScheduling
@EnableWebMvc
@EnableTransactionManagement
@EnableCaching
public class SpringframeworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringframeworkApplication.class, args);
    }

}
