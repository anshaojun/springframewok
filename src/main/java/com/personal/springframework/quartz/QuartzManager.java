package com.personal.springframework.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description:归集的缴费订单在失效时间时查询
 * @author: anshaojun
 * @time: 2021-03-12 10:04
 */
@Slf4j
@Component
public class QuartzManager {

    @Resource
    private Scheduler scheduler;

    /**
     * 定时要执行的方法类。
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void reScheduleJob() {
        // 移除已经执行过
        /*Map<String, Object> param = new HashMap<String, Object>();
        param.put("createrType", PublicOrderOptions.EXTERNAL.getCode());
        param.put("year", Calendar.getInstance().get(Calendar.YEAR));
        param.put("selectWhere", "and parsejson(extra,'hasQueryTask') is not null and loseeffagcytime <" + ("oracle".equalsIgnoreCase(dbname) ? "sysdate" : "now()"));
        List<PublicOrder> dateValues = (List<PublicOrder>) publicOrderMapper.queryByParam(param);
        if (dateValues != null && dateValues.size() > 0) {
            for (PublicOrder atdv : dateValues) {
                //移除任务
                removeExpireTasks(atdv);
            }
        }
        param.put("orderStatus", EduOrderStatusOptions.UNPAY.getCode());
        param.put("repeatPayFlag", PublicOrderOptions.REPEAT.getCode());
        param.put("selectWhere", "and loseeffagcytime >" + ("oracle".equalsIgnoreCase(dbname) ? "sysdate" : "now()"));
        List<PublicOrder> notbegindateValues = (List<PublicOrder>) publicOrderMapper.queryByParam(param);
        if (notbegindateValues == null || notbegindateValues.size() == 0) {
            log.info("查询的任务列表为空");
        } else {
            for (PublicOrder atdv : notbegindateValues) {
                configSchedul(atdv);
            }
        }*/
    }

    /**
     * 移除过期任务
     *
     * @param atdv
     */
    /*private void removeExpireTasks(PublicOrder atdv) {
        try {
            TriggerKey triggerKey = new TriggerKey(atdv.getOrderNumber(), Scheduler.DEFAULT_GROUP);
            JobKey jobKey = new JobKey(atdv.getOrderNumber(), Scheduler.DEFAULT_GROUP);
            Trigger trigger = scheduler.getTrigger(triggerKey);
            if (trigger != null) {
                log.info("==移除任务==" + atdv.getOrderNumber());
                scheduler.pauseTrigger(triggerKey);// 停止触发器
                scheduler.unscheduleJob(triggerKey);// 移除触发器
                scheduler.deleteJob(jobKey);// 删除任务
            }
        } catch (SchedulerException e) {
            log.error("移除任务失败...");
            e.printStackTrace();
        }
    }*/

    /**
     * 配置任务列表
     * @param atdv
     */
    /*private void configSchedul(PublicOrder atdv) {
        try {
            Trigger trigger = scheduler.getTrigger(new TriggerKey(atdv.getOrderNumber(), Scheduler.DEFAULT_GROUP));
            if (trigger == null) {//说明schedule中不存在该定时任务
                createTriggerTask(atdv);
            }
        } catch (SchedulerException e) {
            log.error("获取触发器trigger失败...");
            e.printStackTrace();
        }
    }*/


    /**
     * 创建任务列表
     *
     * @param atdv
     */
    /*private void createTriggerTask(PublicOrder atdv) {
        if (atdv.getLoseEffagcyTime().after(new Date())) {
            try {
                log.info("=创建：=" + atdv.getOrderNumber());
                JobDetail job = JobBuilder.newJob(ExternalOrderQuery.class)
                        .withIdentity(atdv.getOrderNumber(), Scheduler.DEFAULT_GROUP)
                        .usingJobData("orderNumber", atdv.getOrderNumber())
                        .build();
                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(atdv.getOrderNumber(), Scheduler.DEFAULT_GROUP)
                        .startAt(atdv.getLoseEffagcyTime())
                        .build();
                scheduler.scheduleJob(job, trigger);
                scheduler.start();
            } catch (SchedulerException sx) {
                sx.printStackTrace();
                log.info("创建定时任务失败......");
            }
        } else {
            this.removeExpireTasks(atdv);
        }
    }*/
}
