package com.widyu.healthcare.dto.response;

import lombok.Builder;
import java.util.List;

public class MainGoalResponseDto {
    GuardianGoalResponseDto guardianGoalResponseDto;
    List<SeniorGoalResponseDto> seniorGoalResponseDtoList;
    @Builder
    public MainGoalResponseDto(GuardianGoalResponseDto guardianGoalResponseDto, List<SeniorGoalResponseDto> seniorGoalResponseDtoList) {
        this.guardianGoalResponseDto = guardianGoalResponseDto;
        this.seniorGoalResponseDtoList = seniorGoalResponseDtoList;
    }
}