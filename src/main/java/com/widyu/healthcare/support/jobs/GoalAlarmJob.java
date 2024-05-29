package com.widyu.healthcare.support.jobs;

import com.widyu.healthcare.core.domain.domain.v1.GoalStatus;
import com.widyu.healthcare.core.domain.service.v1.FcmService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;

public class GoalAlarmJob implements Job {

    @Autowired
    private final FcmService fcmService;

    public GoalAlarmJob(FcmService fcmService) {
        this.fcmService = fcmService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        LocalDateTime currentTime = LocalDateTime.now();

        JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
        String fcmToken = jobDataMap.getString("fcmToken");

        try {
            fcmService.sendMessage(fcmToken, "목표를 달성할 시간입니다.", "목표를 확인하세요!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
