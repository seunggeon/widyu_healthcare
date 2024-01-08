package com.widyu.healthcare.mapper;

import com.widyu.healthcare.dto.goals.GoalStatus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoalsStatusMapper {

    void insertGoalStatus(GoalStatus goalStatus);

    void deleteGoalStatus(long userIdx, long goalIdx);
    int countGoalStatus(long userIdx, long goalIdx);
}
