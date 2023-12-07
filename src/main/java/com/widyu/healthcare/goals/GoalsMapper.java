package com.widyu.healthcare.goals;

import com.widyu.healthcare.goals.model.Goal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoalsMapper {

    List<Goal> getGoalsById(long userIdx);
    void insertGoal(Goal goal);
    void updateGoal(long goalId, Goal goal);
    void deleteGoal(long userIdx, long goalIdx);

}
