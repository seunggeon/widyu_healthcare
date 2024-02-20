package com.widyu.healthcare.dto.response;

import com.widyu.healthcare.dto.DiseaseDto;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 모든 가족 정보 조회 attribute
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class SeniorDetailResponseDto extends UsersResponseDto {
    private String inviteCode;
    private String phoneNumber;
    private String address;
    private String birth;
    private int isDisease;

    private List<DiseaseDto> diseases;
    public List<SeniorDetailResponseDto> toList() {
        List<SeniorDetailResponseDto> seniorDetailsList = new ArrayList<>();
        seniorDetailsList.add(this);
        return seniorDetailsList;
    }
}
