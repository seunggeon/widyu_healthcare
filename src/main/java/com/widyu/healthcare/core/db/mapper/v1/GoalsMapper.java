package com.widyu.healthcare.core.db.mapper.v1;

import com.widyu.healthcare.core.domain.domain.v1.Goal;
import com.widyu.healthcare.core.api.controller.v1.response.goal.GuardianGoalResponse;
import com.widyu.healthcare.core.api.controller.v1.response.goal.SeniorGoalResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoalsMapper {
    SeniorGoalResponse findBySeniorIdx(long userIdx);
    GuardianGoalResponse findByGuardianIdx(long userIdx);
    List<Goal> getGoalsByUserIdx(@Param("userIdx") long userIdx);
    Goal getGoalByGoalIdx(long goalIdx);
    int insertGoal(Goal goal);
    long getGoalIdx(Goal goal);
    int updateGoal(Goal goal);
    int deleteGoal(long goalIdx);

}