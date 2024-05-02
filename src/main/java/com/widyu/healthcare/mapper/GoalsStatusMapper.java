package com.widyu.healthcare.mapper;

import com.widyu.healthcare.dto.domain.GoalStatusDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoalsStatusMapper {

    void insertGoalStatus(GoalStatusDto goalStatus);

    void deleteGoalStatus(long goalIdx);
    void updateGoalStatusUrl(String url, long goalStatusIdx);
    void updateGoalStatus(GoalStatusDto goalStatus);
    int countGoalStatus(long userIdx, long goalIdx);
    GoalStatusDto getGoalStatusByGoalStatusIdx(long goalStatusIdx);
    List<GoalStatusDto> getGoalStatusesByGoalIdx(long goalIdx);
    String getUrlByGoalStatusIdx(long goalStatusIdx);
    long getGoalStatusIdx(GoalStatusDto goalStatus);
    void updateStatus(long goalStatusIdx, long status);
    void updateTotalPoint(long userIdx, long value);
}
