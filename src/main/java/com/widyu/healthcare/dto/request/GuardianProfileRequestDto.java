package com.widyu.healthcare.dto.request;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * 보호자 프로필 수정
 */
@Getter
public class GuardianProfileRequestDto {
    @Nullable
    private String name;
    @Nullable
    private String profileImageUrl;
    @Nullable
    private String phoneNumber;
    @Nullable
    private String address;
    @Nullable
    private String birth;
    @Builder
    public GuardianProfileRequestDto(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
