package com.widyu.healthcare.goals;

import com.widyu.healthcare.goals.model.Goal;
import com.widyu.healthcare.goals.model.GoalStatus;
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
