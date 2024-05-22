package com.widyu.healthcare.core.api.controller.v1.request.guardian;

import com.widyu.healthcare.core.domain.domain.v1.User;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;

import static com.widyu.healthcare.core.domain.domain.v1.UserType.SENIOR;

/**
 * 보호자 프로필 수정
 */
@Getter
public class UpdateGuardianProfileRequest {
    @Nullable
    private String name;
    @Nullable
    private String phoneNumber;
    @Nullable
    private String address;
    @Nullable
    private String birth;
    @Builder
    public UpdateGuardianProfileRequest(String name, String phoneNumber, String address, String birth) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birth = birth;
    }
    public User toUser() {
        User user = User.builder()
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .birth(this.birth)
                .address(this.address)
                .build();
        return user;
    }
}
