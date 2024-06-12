package com.widyu.healthcare.core.api.controller.v1.request.guardian;

import com.widyu.healthcare.core.domain.domain.v1.User;
import com.widyu.healthcare.core.domain.domain.v1.UserDetail;
import com.widyu.healthcare.support.utils.SHA256Util;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NonNull;
@Getter
public class LoginGuardianRequest {
    @NonNull
    private String id;
    @NonNull
    private String password;
    @Size(max = 300, message = "fcmToken should be up to 300 characters")
    private String fcmToken;
    public UserDetail toUserDetail() {
        UserDetail userDetail = UserDetail.builder()
                .id(this.id)
                .password(SHA256Util.encryptSHA256(this.password))
                .fcmToken(this.fcmToken)
                .build();
        return userDetail;
    }
}