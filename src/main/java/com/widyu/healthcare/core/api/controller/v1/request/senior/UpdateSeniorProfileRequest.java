package com.widyu.healthcare.core.api.controller.v1.request.senior;

import com.widyu.healthcare.core.domain.domain.v1.Disease;
import com.widyu.healthcare.core.domain.domain.v1.User;
import jakarta.annotation.Nullable;
import lombok.Getter;

import java.util.List;

import static com.widyu.healthcare.core.domain.domain.v1.UserType.SENIOR;

/**
 * 시니어 프로필 수정 (일부)
 * @param
 */
@Getter
public class UpdateSeniorProfileRequest {
    @Nullable
    private String name;
    @Nullable
    private String birth;
    @Nullable
    private String phoneNumber;
    @Nullable
    private String address;
    @Nullable
    private Boolean isDisease;
    @Nullable
    private List<Disease> diseases;
    public User toUser() {
        User user = User.builder()
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .birth(this.birth)
                .address(this.address)
                .type(SENIOR)
                .isDisease(this.isDisease)
                .diseases(this.diseases)
                .build();
        return user;
    }
}
