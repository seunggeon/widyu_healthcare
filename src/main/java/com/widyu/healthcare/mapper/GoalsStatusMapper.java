package com.widyu.healthcare.mapper;

import com.widyu.healthcare.dto.goals.GoalStatus;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface GoalsStatusMapper {

    long insertGoalStatus(GoalStatus goalStatus);

    void deleteGoalStatus(long goalIdx);
    void updateGoalStatusUrl(String url, long goalIdx, LocalDateTime time);
    void updateGoalStatus(GoalStatus goalStatus);
    int countGoalStatus(long userIdx, long goalIdx);
    GoalStatus getGoalStatusByGoalId(long goalIdx, LocalDateTime time);
    long getGoalStatusIdx(GoalStatus goalStatus);
    void updateStatusSuccess(long goalStatusIdx);

}
