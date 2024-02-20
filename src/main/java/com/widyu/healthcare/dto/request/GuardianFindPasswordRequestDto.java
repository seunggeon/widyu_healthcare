package com.widyu.healthcare.dto.request;

import lombok.Getter;
import lombok.NonNull;

/**
 * 보호자 패스워드 찾기
 */
@Getter
public class GuardianFindPasswordRequestDto {
    @NonNull
    private String name;
    @NonNull
    private String phoneNumber;
    @NonNull
    private String id;
}
