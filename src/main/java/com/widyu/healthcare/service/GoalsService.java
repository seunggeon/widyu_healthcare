package com.widyu.healthcare.service;

import com.widyu.healthcare.mapper.GoalsStatusMapper;
import com.widyu.healthcare.dto.goals.Goal;
import com.widyu.healthcare.dto.goals.GoalStatus;
import com.widyu.healthcare.mapper.GoalsMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Log4j2
@Service
public class GoalsService {

    private final GoalsMapper goalsMapper;
    private final GoalsStatusMapper goalsStatusMapper;

    @Autowired
    public GoalsService(GoalsMapper goalsMapper, GoalsStatusMapper goalsStatusMapper) {

        this.goalsMapper = goalsMapper;
        this.goalsStatusMapper = goalsStatusMapper;
    }

    // 목표 전체 조회
    public List<Goal> getGoalsById(long id){

        return goalsMapper.getGoalsById(id);
    }

    // 특정 단일 목표 조회
    public Goal getGoalByGoalId(long userIdx, long goalIdx){

        return goalsMapper.getGoalByGoalId(userIdx, goalIdx);
    }

    // 목표 생성
    public Goal insertGoal(Goal goal){
        goalsMapper.insertGoal(goal);
        Long goalIdx = goalsMapper.getGoalIdx(goal);

        for (GoalStatus goalStatus : goal.getGoalStatusList()) {
            goalStatus.setGoalIdx(goalIdx);
            goalsStatusMapper.insertGoalStatus(goalStatus);
            Long goalStatusIdx = goalsStatusMapper.getGoalStatusIdx(goalStatus);
            goalStatus.setGoalStatusIdx(goalStatusIdx);
        }
        return goal;
    }

    // 목표 수정
    public void updateGoal(Goal goal){

        goalsMapper.updateGoal(goal);
        for (GoalStatus goalStatus : goal.getGoalStatusList()) {
            goalsStatusMapper.updateGoalStatus(goalStatus);
        }
    }

    // 목표 삭제
    public void deleteGoal(long userIdx, long goalIdx){
        goalsStatusMapper.deleteGoalStatus(goalIdx);
        goalsMapper.deleteGoal(userIdx, goalIdx);
    }

    // 목표 상태 수정 (성공)
    public void updateStatusSuccess(long goalStatusIdx){
        goalsStatusMapper.updateStatusSuccess(goalStatusIdx);
    }
}
