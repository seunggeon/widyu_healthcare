package com.widyu.healthcare.service;

import com.widyu.healthcare.mapper.GoalsStatusMapper;
import com.widyu.healthcare.dto.goals.Goal;
import com.widyu.healthcare.dto.goals.GoalStatus;
import com.widyu.healthcare.mapper.GoalsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void insertGoal(Goal goal){
        goalsMapper.insertGoal(goal);
        for (GoalStatus goalStatus : goal.getGoalStatusList()) {
            goalsStatusMapper.insertGoalStatus(goalStatus);
        }
    }

    // 목표 수정
    public void updateGoal(long goalId, Goal goal){

        goalsMapper.updateGoal(goalId, goal);
    }

    // 목표 삭제
    public void deleteGoal(long userIdx, long goalIdx){
        goalsStatusMapper.deleteGoalStatus(userIdx, goalIdx);
        goalsMapper.deleteGoal(userIdx, goalIdx);
    }
}
