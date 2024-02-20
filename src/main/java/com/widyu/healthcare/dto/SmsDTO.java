package com.widyu.healthcare.dto;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class SmsDTO {

    @NonNull
    private String phoneNumber;
}


