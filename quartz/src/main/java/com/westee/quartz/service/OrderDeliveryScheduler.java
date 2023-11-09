package com.westee.quartz.service;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderDeliveryScheduler {
    private final Scheduler scheduler;

    public OrderDeliveryScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void scheduleOrderDelivery(Date cronExpression, String name, String orderId) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("date", cronExpression.toString());
        jobDataMap.put("name", name);
        JobDetail jobDetail = JobBuilder.newJob(OrderDeliveryJob.class)
                .withIdentity("orderDeliveryJobId-" + orderId)
                .usingJobData(jobDataMap)
                .build();
        System.out.println(cronExpression);
        Trigger trigger = OrderDeliveryTrigger.createTrigger(jobDetail, cronExpression);
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public void cancelOrderDelivery(String orderId) {
        JobKey jobKey = new JobKey("orderDeliveryJobId-" + orderId);
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public void rescheduleOrderDelivery(String orderId, Date newCronExpression) {
        JobKey jobKey = new JobKey("orderDeliveryJob" + orderId);
        JobDetail jobDetail = null;
        try {
            jobDetail = scheduler.getJobDetail(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        String name = (String) jobDetail.getJobDataMap().get("name");
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        scheduleOrderDelivery(newCronExpression, name, orderId);
    }

}
