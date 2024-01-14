package com.widyu.healthcare.mapper;

import com.widyu.healthcare.dto.goals.Goal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoalsMapper {

    List<Goal> getGoalsById(long userIdx);
    Goal getGoalByGoalId(long userIdx, long goalIdx);
    long insertGoal(Goal goal);
    long getGoalIdx(Goal goal);
    void updateGoal(Goal goal);
    void deleteGoal(long userIdx, long goalIdx);

}
