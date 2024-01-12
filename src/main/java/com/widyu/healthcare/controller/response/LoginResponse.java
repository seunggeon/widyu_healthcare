package com.widyu.healthcare.controller.response;

import com.widyu.healthcare.dto.users.UsersDTO;
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
    public static final LoginResponse DELETED = new LoginResponse(LoginStatus.DELETED);

    // userInfo return
    public static LoginResponse success(UsersDTO userInfo) {
        return new LoginResponse(LoginStatus.SUCCESS, userInfo);
    }
}