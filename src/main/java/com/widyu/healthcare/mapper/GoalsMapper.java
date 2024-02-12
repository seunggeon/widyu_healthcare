package com.widyu.healthcare.mapper;

import com.widyu.healthcare.dto.goals.GoalDTO;
import com.widyu.healthcare.dto.goals.GoalSetDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface GoalsMapper {

    List<GoalDTO> getGoalsByIdx(long userIdx);
    GoalDTO getGoalByGoalIdx(long userIdx, long goalIdx);
    long insertGoal(GoalDTO goal);
    long getGoalIdx(GoalDTO goal);
    void updateGoal(GoalDTO goal);
    void deleteGoal(long userIdx, long goalIdx);

}
