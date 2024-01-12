package com.widyu.healthcare.mapper;

import com.widyu.healthcare.dto.goals.GoalStatus;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface GoalsStatusMapper {

    void insertGoalStatus(GoalStatus goalStatus);

    void deleteGoalStatus(long userIdx, long goalIdx);
    void updateGoalStatusUrl(String url, long goalIdx, LocalDateTime time);
    int countGoalStatus(long userIdx, long goalIdx);
    GoalStatus getGoalStatusByGoalId(long goalIdx, LocalDateTime time);
}
