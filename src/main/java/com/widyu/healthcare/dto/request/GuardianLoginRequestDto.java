package com.widyu.healthcare.dto.request;

import lombok.Getter;
import lombok.NonNull;
@Getter
public class GuardianLoginRequestDto {
    @NonNull
    private String id;
    @NonNull
    private String password;
    @NonNull
    private String fcmToken;
}