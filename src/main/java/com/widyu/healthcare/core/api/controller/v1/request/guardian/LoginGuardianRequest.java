package com.widyu.healthcare.core.api.controller.v1.request.guardian;

import com.widyu.healthcare.core.domain.domain.v1.User;
import com.widyu.healthcare.core.domain.domain.v1.UserDetail;
import lombok.Getter;
import lombok.NonNull;
@Getter
public class LoginGuardianRequest {
    @NonNull
    private String id;
    @NonNull
    private String password;
    @NonNull
    private String fcmToken;
    public UserDetail toUserDetail() {
        UserDetail userDetail = UserDetail.builder()
                .id(this.id)
                .password(this.password)
                .fcmToken(this.fcmToken)
                .build();
        return userDetail;
    }
}