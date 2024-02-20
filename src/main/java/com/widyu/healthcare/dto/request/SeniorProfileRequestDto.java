package com.widyu.healthcare.dto.request;

import com.widyu.healthcare.dto.DiseaseDto;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

/**
 * 시니어 프로필 수정 (일부)
 * @param
 */
@Getter
public class SeniorProfileRequestDto {
    @Nullable
    private String name;
    @Nullable
    private String profileImageUrl;
    @Nullable
    private String birth;
    @Nullable
    private String phoneNumber;
    @Nullable
    private String address;
    @Nullable
    private int isDisease;
    @Nullable
    private List<DiseaseDto> diseases;
}
