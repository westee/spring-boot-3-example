package com.westee.quartz.service;

import org.quartz.*;

import java.util.Date;

public class OrderDeliveryTrigger {
    public static Trigger createTrigger(JobDetail jobDetail, String cronExpression) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .withIdentity("orderDeliveryTrigger")
                .withDescription("Trigger for order delivery")
                .build();
    }

    public static Trigger createTrigger(JobDetail jobDetail, Date startTime) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .withIdentity("orderDeliveryTrigger")
                .withDescription("指定时间订单派送触发器")
                .startAt(startTime)
                .build();
    }
}
