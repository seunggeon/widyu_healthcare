package com.widyu.healthcare.core.api.controller.v1.request.senior;

import lombok.Getter;
import lombok.NonNull;
@Getter
public class LoginSeniorRequest {
    @NonNull
    private String inviteCode;
    private String fcmToken;
}
