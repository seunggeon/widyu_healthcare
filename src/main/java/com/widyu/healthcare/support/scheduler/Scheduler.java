package com.widyu.healthcare.support.scheduler;

import com.widyu.healthcare.core.db.mapper.v1.GoalsStatusMapper;
import com.widyu.healthcare.core.domain.domain.v1.GoalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
@RequiredArgsConstructor
@Component
public class Scheduler {
    private final GoalsStatusMapper goalsStatusMapper;
    @Scheduled(cron = "0 0 0 * * ?") // 요일 상관없이 00시 00분 00초에 실행
    public void generateGoalStatusAtMidnight() {
        // DB에 생성된 goalstatusIdx 조회 및 정보 받아오기 -> 과거에 생성된 적이 있던 애들은 제외
        List<GoalStatus> goalStatusList = goalsStatusMapper.findStatusNotRegenerated();

        if(goalStatusList == null) return;

        // 각자 새롭게 생성
        goalStatusList.forEach(goalStatus -> {
            long goalStatusIdx = goalStatus.getGoalStatusIdx();
            int insertResult = goalsStatusMapper.insertGoalStatus(goalStatus);
            if (insertResult > 0) {
                goalsStatusMapper.updateRegenerateComplete(goalStatusIdx);
            } else {
                throw new RuntimeException();
            }
        });
    }
}
