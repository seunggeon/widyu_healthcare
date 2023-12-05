package com.widyu.healthcare.users.dto;
import com.widyu.healthcare.global.annotation.LoginCheck.UserLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class UserDTO {
    public enum Status {
        DEFAULT, DELETED
    }

    @NonNull
    private String id;

    @NonNull
    private String password;

    private String email;

    private String name;

    private String phone;

    private String address;

    private UserLevel level;

    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static boolean hasNullDataBeforeSignup(UserDTO userInfo) {
        return userInfo.getId() == null || userInfo.getPassword() == null
                || userInfo.getName() == null || userInfo.getEmail() == null
                || userInfo.getPhone() == null;
    }
}