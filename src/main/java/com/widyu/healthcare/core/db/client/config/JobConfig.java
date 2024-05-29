package com.widyu.healthcare.core.db.client.config;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Configuration
public class JobConfig {

    @Bean
    public Scheduler scheduler() {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            return scheduler;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JobDetail createJobDetail(Class<? extends Job> jobClass, String jobName, Map<String, Object> jobData) {
        JobBuilder jobBuilder = JobBuilder.newJob(jobClass)
                .withIdentity(jobName);

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(jobData);
        return jobBuilder.usingJobData(jobDataMap).build();
    }

    public Trigger createTrigger(String triggerName, Map<String, Object> jobData, Date startTime) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(jobData);

        return TriggerBuilder.newTrigger()
                .usingJobData(jobDataMap)
                .withIdentity(triggerName)
                .startAt(startTime)
                .build();
    }
}
