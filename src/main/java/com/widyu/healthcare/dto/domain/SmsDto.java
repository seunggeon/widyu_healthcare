package com.widyu.healthcare.dto.domain;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class SmsDto {

    @NonNull
    private String phoneNumber;
}


