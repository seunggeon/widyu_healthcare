package com.widyu.healthcare.core.api.controller.v1.request.guardian;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * 보호자 id 찾기
 */
@Getter
public class FindGuardianIdRequest {
    @NonNull
    private String name;
    @NonNull
    private String phoneNumber;

    @Builder
    public FindGuardianIdRequest(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
