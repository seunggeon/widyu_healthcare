package com.widyu.healthcare.core.db.mapper.v1;

import com.widyu.healthcare.core.domain.domain.v1.GoalStatus;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoalsStatusMapper {

    void insertGoalStatus(GoalStatus goalStatus);
    void deleteGoalStatus(long goalIdx);
    void updateGoalStatusUrl(String url, long goalStatusIdx);
    void updateGoalStatus(GoalStatus goalStatus);
    int countGoalStatus(long userIdx, long goalIdx);
    GoalStatus getGoalStatusByGoalStatusIdx(long goalStatusIdx);
    List<GoalStatus> getGoalStatusesByGoalIdx(long goalIdx);
    String getUrlByGoalStatusIdx(long goalStatusIdx);
    long getGoalStatusIdx(GoalStatus goalStatus);
    int updateStatus(long goalStatusIdx, long status);
    int updateTotalPoint(long userIdx, long value);
}
