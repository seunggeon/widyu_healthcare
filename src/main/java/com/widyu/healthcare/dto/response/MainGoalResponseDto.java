package com.widyu.healthcare.dto.response;

import lombok.Builder;
import java.util.List;

public class MainGoalResponseDto {
    GuardianGoalResponseDto guardianGoalResponseDto;
    List<SeniorGoalResponseDto> seniorGoalResponseDtos;
    @Builder
    public MainGoalResponseDto(GuardianGoalResponseDto guardianGoalResponseDtos, List<SeniorGoalResponseDto> seniorGoalResponseDtos) {
        this.guardianGoalResponseDto = guardianGoalResponseDtos;
        this.seniorGoalResponseDtos = seniorGoalResponseDtos;
    }
}