package com.widyu.healthcare.dto.request;

import lombok.Getter;
import lombok.NonNull;
@Getter
public class SeniorLoginRequestDto {
    @NonNull
    private String inviteCode;
    private String fcmToken;
}
