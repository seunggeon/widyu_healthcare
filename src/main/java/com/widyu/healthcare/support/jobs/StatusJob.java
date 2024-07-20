package com.widyu.healthcare.support.jobs;

import com.widyu.healthcare.core.api.controller.v1.response.guardian.GuardianInfoResponse;
import com.widyu.healthcare.core.db.mapper.v1.GoalsMapper;
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
    private GoalsMapper goalsMapper;
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
        String userName = jobDataMap.getString("userName");

        if (goalStatusIdx != null) {
            GoalStatus goalStatus = goalsStatusMapper.getGoalStatusByGoalStatusIdx(goalStatusIdx);
            if (goalStatus.getStatus() == 0){
                goalsStatusMapper.updateStatus(goalStatusIdx, -1L);
                long goalIdx = goalStatus.getGoalIdx();
                //시니어 fcm 알림
                try {
                    // 푸쉬 알림(senior): 시니어 본인에게 목표 실패
                    fcmService.sendMessage(seniorsMapper.findFCM(userIdx), FcmService.Situation.GOAL_FAILURE, userName, goalsMapper.getGoalByGoalIdx(goalIdx).getTitle());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // 푸쉬 알림(guardian): 가디언에게 목표 실패s
                fcmService.sendMessageToGuardians(userIdx, FcmService.Situation.GOAL_FAILURE, userName, goalsMapper.getGoalByGoalIdx(goalIdx).getTitle());
            }
        } else {
            log.error("goalStatusIdx is null");
        }
    }
}