package com.widyu.healthcare.mapper;

import com.widyu.healthcare.dto.goals.GoalDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoalsMapper {

    List<GoalDTO> getGoalsById(long userIdx);
    GoalDTO getGoalByGoalId(long userIdx, long goalIdx);
    long insertGoal(GoalDTO goal);
    long getGoalIdx(GoalDTO goal);
    void updateGoal(GoalDTO goal);
    void deleteGoal(long userIdx, long goalIdx);

}
