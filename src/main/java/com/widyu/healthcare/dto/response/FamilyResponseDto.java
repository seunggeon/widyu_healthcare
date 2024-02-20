package com.widyu.healthcare.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
public class FamilyResponseDto {
    List<GuardianDetailResponseDto> guardianDetailResponseDtos;
    List<SeniorDetailResponseDto> seniorDetailResponseDtos;
    @Builder
    public FamilyResponseDto(List<GuardianDetailResponseDto> guardianDetailResponseDtos, List<SeniorDetailResponseDto> seniorDetailResponseDtos) {
        this.guardianDetailResponseDtos = guardianDetailResponseDtos;
        this.seniorDetailResponseDtos = seniorDetailResponseDtos;
    }
}
