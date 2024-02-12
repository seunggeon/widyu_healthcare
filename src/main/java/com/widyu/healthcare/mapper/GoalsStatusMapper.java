package com.widyu.healthcare.mapper;

import com.widyu.healthcare.dto.goals.GoalStatusDTO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface GoalsStatusMapper {

    void insertGoalStatus(GoalStatusDTO goalStatus);

    void deleteGoalStatus(long goalIdx);
    void updateGoalStatusUrl(String url, long goalStatusIdx);
    void updateGoalStatus(GoalStatusDTO goalStatus);
    int countGoalStatus(long userIdx, long goalIdx);
    GoalStatusDTO getGoalStatusByGoalStatusIdx(long goalStatusIdx);
    List<GoalStatusDTO> getGoalStatusesByGoalIdx(long goalIdx);
    String getUrlByGoalStatusIdx(long goalStatusIdx);
    long getGoalStatusIdx(GoalStatusDTO goalStatus);
    void updateStatus(long goalStatusIdx, long status);
    void updateTotalPoint(long userIdx, long value);
}
