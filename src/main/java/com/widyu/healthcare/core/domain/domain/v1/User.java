package com.widyu.healthcare.core.domain.domain.v1;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
public class User extends UserDetail {

    private long userIdx;
    private String name;
    private String phoneNumber;
    private int age;
    private String birth;
    private String address;
    private String inviteCode;
    private UserType type;
    private Boolean isDisease;
    private List<Disease> diseases;
}