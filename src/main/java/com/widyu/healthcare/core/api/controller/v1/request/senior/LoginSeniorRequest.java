package com.widyu.healthcare.core.api.controller.v1.request.senior;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NonNull;
@Getter
public class LoginSeniorRequest {
    @Min(value = 6)
    @Max(value = 6)
    private String inviteCode;
    @Size(max = 300, message = "fcmToken should be up to 300 characters")
    private String fcmToken;
}
