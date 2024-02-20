package com.widyu.healthcare.dto.request;

import com.widyu.healthcare.aop.LoginCheck;
import com.widyu.healthcare.dto.DiseaseDto;
import com.widyu.healthcare.dto.UserType;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 시니어 회원가입
 * 시니어 추가 등록
 * @param
 */
@Getter
public class SeniorRequestDto {
    @Nullable
    private long userIdx;
    @NonNull
    private String name;
    @NonNull
    private String phoneNumber;
    @NonNull
    private int age;
    @NonNull
    private String birth;
    @NonNull
    private String address;
    @Nullable
    private String inviteCode;
    @Nullable
    private UserType type;
    @NonNull
    private Boolean isDisease;
    @Nullable
    private List<DiseaseDto> diseases;
    @Builder
    public SeniorRequestDto(long userIdx, String name, String phoneNumber, int age, String birth, String address, String inviteCode, UserType type, Boolean isDisease, List<DiseaseDto> diseases) {
        this.userIdx = userIdx;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.birth = birth;
        this.address = address;
        this.inviteCode = inviteCode;
        this.type = type;
        this.isDisease = isDisease;
        this.diseases = diseases;
    }
}
