package com.widyu.healthcare.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class SmsDTO {

    @NonNull
    private String phoneNumber;
}


