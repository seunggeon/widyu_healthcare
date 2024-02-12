package com.widyu.healthcare.jobs;

import com.widyu.healthcare.dto.goals.GoalStatusDTO;
import com.widyu.healthcare.mapper.GoalsStatusMapper;
import com.widyu.healthcare.service.GoalsService;
import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Log4j2
@Component
public class StatusJob implements Job {

    @Autowired
    private GoalsService goalsService;
    @Autowired
    private GoalsStatusMapper goalsStatusMapper;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.info("statusJob");
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println("현재 시간: " + currentTime);

        JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
        Long goalStatusIdx = jobDataMap.getLong("goalStatusIdx");
        log.info("goalStatusIdx: {}", goalStatusIdx);


        if (goalStatusIdx != null) {
            GoalStatusDTO goalStatus = goalsStatusMapper.getGoalStatusByGoalStatusIdx(goalStatusIdx);
            if (goalStatus.getStatus() == 0){
                goalsStatusMapper.updateStatus(goalStatusIdx, -1L);
            }
        } else {
            log.error("goalStatusIdx is null");
        }



    }
}
