package com.widyu.healthcare.core.api.controller.v1.request.guardian;

import com.widyu.healthcare.core.domain.domain.v1.User;
import com.widyu.healthcare.support.utils.SHA256Util;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * 보호자 회원가입
 */
@Getter
public class RegisterGuardianRequest {
    @NonNull
    private long userIdx;
    @NonNull
    private String name;
    @NonNull
    private String phoneNumber;
    @NonNull
    private String id;
    @NonNull
    private String password;
    @Builder
    public RegisterGuardianRequest(long userIdx, String name, String phoneNumber, String id, String password) {
        this.userIdx = userIdx;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.id = id;
        this.password = password;
    }
    public User toEncryptedUser() {
        User user = User.builder()
                .userIdx(this.userIdx)
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .id(this.id)
                .password(SHA256Util.encryptSHA256(this.password))
                .build();
        return user;
    }
}
