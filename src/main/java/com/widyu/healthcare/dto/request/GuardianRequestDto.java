package com.widyu.healthcare.dto.request;

import com.widyu.healthcare.aop.LoginCheck;
import com.widyu.healthcare.dto.UserStatus;
import com.widyu.healthcare.dto.UserType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * 보호자 회원가입
 */
@Getter
public class GuardianRequestDto {
    @NonNull
    private String name;
    @NonNull
    private String phoneNumber;
    @NonNull
    private String id;
    @NonNull
    private String password;
    @Builder
    public GuardianRequestDto(String name, String phoneNumber, String id, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.id = id;
        this.password = password;
    }
}
