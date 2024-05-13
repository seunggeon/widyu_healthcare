package com.widyu.healthcare.core.api.controller.v1.request.guardian;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * 보호자 패스워드 찾기
 */
@Getter
public class FindGuardianPasswordRequest {
    @NonNull
    private String name;
    @NonNull
    private String phoneNumber;
    @NonNull
    private String id;
    @NonNull
    private String newPassword;
    @Builder
    public FindGuardianPasswordRequest(String name, String phoneNumber, String id, String newPassword) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.id = id;
        this.newPassword = newPassword;
    }
}
