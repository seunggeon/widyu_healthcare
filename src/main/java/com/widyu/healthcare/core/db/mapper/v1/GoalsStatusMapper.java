package com.widyu.healthcare.core.db.mapper.v1;

import com.widyu.healthcare.core.domain.domain.v1.GoalStatus;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface GoalsStatusMapper {

    int insertGoalStatus(GoalStatus goalStatus);
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

    Double getGoalRateDaily(long userIdx, int month, int day);
    int getGoalCntDaily(long userIdx, int month, int day);
    List<Map<Integer, Double>> getGoalRateMonthly(long userIdx, int month);

    List<GoalStatus> findStatusNotRegenerated();
    void updateRegenerateComplete(long goalStatusIdx);
}
