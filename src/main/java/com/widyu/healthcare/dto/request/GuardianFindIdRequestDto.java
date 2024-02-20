package com.widyu.healthcare.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * 보호자 id 찾기
 */
@Getter
public class GuardianFindIdRequestDto {
    @NonNull
    private String name;
    @NonNull
    private String phoneNumber;

    @Builder
    public GuardianFindIdRequestDto(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
