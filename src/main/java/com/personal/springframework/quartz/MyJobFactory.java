package com.personal.springframework.quartz;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description:
 * @author: anshaojun
 * @time: 2021-04-22 18:36
 */
@Component
public class MyJobFactory extends SpringBeanJobFactory {
    //将不由spring管理生命周期的bean注入
    //由于quartz由spring创建，但quartz创建的任务不在spring的生命周期中
    @Resource
    private AutowireCapableBeanFactory capableBeanFactory;

    //重写创建实例方法，将创建的实例注入到spring的生命周期中
    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        // 调用父类的方法
        Object jobInstance = super.createJobInstance(bundle);
        // 进行注入
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
