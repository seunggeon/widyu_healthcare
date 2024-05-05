package com.widyu.healthcare.mapper;

import com.widyu.healthcare.dto.domain.GoalDto;
import com.widyu.healthcare.dto.response.GuardianGoalResponseDto;
import com.widyu.healthcare.dto.response.SeniorGoalResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoalsMapper {
    SeniorGoalResponseDto findBySeniorIdx(long userIdx);
    GuardianGoalResponseDto findByGuardianIdx(long userIdx);
    List<GoalDto> getGoalsByIdx(long userIdx);
    GoalDto getGoalByGoalIdx(long userIdx, long goalIdx);
    void insertGoal(GoalDto goal);
    long getGoalIdx(GoalDto goal);
    void updateGoal(GoalDto goal);
    void deleteGoal(long userIdx, long goalIdx);

}