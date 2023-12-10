package com.widyu.healthcare.users.dto;
import com.widyu.healthcare.global.aop.LoginCheck.UserType;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UsersDTO {
    public enum Status {
        ACTIVE, DELETED
    }

    private String userIdx;

    @NonNull
    private String id;

    @NonNull
    private String password;

    private String phone;

    private String name;

    private String birth;

    private Integer age;

    private String address;

    private String inviteCode;

    private UserType type;

    @Nullable
    private String tagColor;

    private Integer isDisease;

    @Nullable
    private Status status;

    public static boolean hasNullDataBeforeSeniorSignup(UsersDTO userInfo) {
        return  userInfo.getName() == null || userInfo.getBirth() == null
                || userInfo.getPhone() == null || userInfo.getAge() == null
                || userInfo.getAddress() == null || userInfo.getIsDisease() == null;
    }
    public static boolean hasNullDataBeforeGuardianSignup(UsersDTO userInfo) {
        return userInfo.getId() == null || userInfo.getPassword() == null;
    }
}