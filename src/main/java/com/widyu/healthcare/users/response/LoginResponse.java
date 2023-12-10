package com.widyu.healthcare.users.response;

import com.widyu.healthcare.users.dto.UsersDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginResponse {
    enum LoginStatus {
        SUCCESS, FAIL, DELETED
    }

    @NonNull
    private LoginStatus result;
    private UsersDTO userInfo;

    public static final LoginResponse FAIL = new LoginResponse(LoginStatus.FAIL);
    public static LoginResponse success(UsersDTO userInfo) {
        return new LoginResponse(LoginStatus.SUCCESS, userInfo);
    }
}