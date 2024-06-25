package com.widyu.healthcare.support.jobs;

import com.widyu.healthcare.core.api.controller.v1.response.guardian.GuardianInfoResponse;
import com.widyu.healthcare.core.db.mapper.v1.SeniorsMapper;
import com.widyu.healthcare.core.domain.domain.v1.GoalStatus;
import com.widyu.healthcare.core.db.mapper.v1.GoalsStatusMapper;
import com.widyu.healthcare.core.domain.service.v1.FcmService;
import com.widyu.healthcare.core.domain.service.v1.GoalsService;
import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@Log4j2
@Component
public class StatusJob implements Job {

    @Autowired
    private GoalsService goalsService;
    @Autowired
    private GoalsStatusMapper goalsStatusMapper;
    @Autowired
    private FcmService fcmService;
    @Autowired
    private SeniorsMapper seniorsMapper;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        LocalDateTime currentTime = LocalDateTime.now();

        JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
        Long goalStatusIdx = jobDataMap.getLong("goalStatusIdx");
        Long userIdx = jobDataMap.getLong("userIdx");

        if (goalStatusIdx != null) {
            GoalStatus goalStatus = goalsStatusMapper.getGoalStatusByGoalStatusIdx(goalStatusIdx);
            if (goalStatus.getStatus() == 0){
                goalsStatusMapper.updateStatus(goalStatusIdx, -1L);

                //시니어 fcm 알림
                try {
                    fcmService.sendMessage(seniorsMapper.findFCM(userIdx), "목표 실패", "FAIL");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                //가디언 fcm 알림
                List<GuardianInfoResponse> guardians = seniorsMapper.findGuardiansByIdx(userIdx);
                guardians.forEach(guardian -> {
                    try {
                        fcmService.sendMessage(seniorsMapper.findFCM(userIdx), "목표 실패", "FAIL");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } else {
            log.error("goalStatusIdx is null");
        }
    }
}