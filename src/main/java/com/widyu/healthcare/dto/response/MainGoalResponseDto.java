package com.widyu.healthcare.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MainGoalResponseDto {
    @JsonProperty("guardianGoalList")
    GuardianGoalResponseDto guardianGoalResponseDto;
    @JsonProperty("seniorsGoalList")
    List<SeniorGoalResponseDto> seniorGoalResponseDtoList;
    @Builder
    public MainGoalResponseDto(GuardianGoalResponseDto guardianGoalResponseDto, List<SeniorGoalResponseDto> seniorGoalResponseDtoList) {
        this.guardianGoalResponseDto = guardianGoalResponseDto;
        this.seniorGoalResponseDtoList = seniorGoalResponseDtoList;
    }
}