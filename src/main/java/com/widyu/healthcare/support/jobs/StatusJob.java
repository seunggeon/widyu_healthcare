package com.widyu.healthcare.support.jobs;

import com.widyu.healthcare.core.domain.domain.v1.GoalStatus;
import com.widyu.healthcare.core.db.mapper.v1.GoalsStatusMapper;
import com.widyu.healthcare.core.domain.service.v1.GoalsService;
import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Log4j2
@Component
public class StatusJob implements Job {


    @Autowired
    private final GoalsStatusMapper goalsStatusMapper;

    public StatusJob(GoalsStatusMapper goalsStatusMapper) {
        this.goalsStatusMapper = goalsStatusMapper;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
        Long goalStatusIdx = jobDataMap.getLong("goalStatusIdx");

        if (goalStatusIdx != null) {
            GoalStatus goalStatus = goalsStatusMapper.getGoalStatusByGoalStatusIdx(goalStatusIdx);
            if (goalStatus.getStatus() == 0){
                goalsStatusMapper.updateStatus(goalStatusIdx, -1L);
            }
        } else {
            log.error("goalStatusIdx is null");
        }
    }
}
