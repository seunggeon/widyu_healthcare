package com.widyu.healthcare.dto.request;

import com.widyu.healthcare.dto.domain.GoalDto;
import com.widyu.healthcare.dto.domain.GoalStatusDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
public class GoalSetRequestDto {

    @NonNull
    private GoalDto goalDto;
    @NonNull
    private List<GoalStatusDto> goalStatusDtoList;
    @Builder
    public GoalSetRequestDto(GoalDto goalDto, List<GoalStatusDto> goalStatusDtoList) {
        this.goalDto = goalDto;
        this.goalStatusDtoList = goalStatusDtoList;
    }
}