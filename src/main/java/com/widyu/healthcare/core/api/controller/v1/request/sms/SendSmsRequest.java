package com.widyu.healthcare.core.api.controller.v1.request.sms;

import lombok.Getter;
import lombok.NonNull;
@Getter
public class SendSmsRequest {
    @NonNull
    private String name;
    @NonNull
    private String phoneNumber;
}