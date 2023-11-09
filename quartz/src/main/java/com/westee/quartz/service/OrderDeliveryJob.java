package com.westee.quartz.service;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;

public class OrderDeliveryJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        JobKey jobKey = context.getJobDetail().getKey();
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String name = (String) jobDataMap.get("name");
        System.out.println(jobKey);
        System.out.println(name);
    }
}
