package com.widyu.healthcare.dto.response;

import com.widyu.healthcare.dto.domain.GoalDto;
import com.widyu.healthcare.dto.domain.GoalStatusDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
public class GoalSetResponseDto {
    @NonNull
    private GoalDto goalDto;
    @NonNull
    private List<GoalStatusDto> goalStatusDtoList;
    @Builder
    public GoalSetResponseDto(GoalDto goalDto, List<GoalStatusDto> goalStatusDtoList) {
        this.goalDto = goalDto;
        this.goalStatusDtoList = goalStatusDtoList;
    }
}