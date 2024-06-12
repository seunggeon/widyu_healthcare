package com.widyu.healthcare.core.api.controller.v1.request.sms;

import lombok.Getter;
import lombok.NonNull;
@Getter
public class VerifyRequest {
    @NonNull
    private String certificationCode;
}
