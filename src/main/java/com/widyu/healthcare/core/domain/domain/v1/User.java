package com.widyu.healthcare.core.domain.domain.v1;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class User extends UserDetail {
    @Nullable
    private long userIdx;
    @NonNull
    private String name;
    @NonNull
    private String phoneNumber;
    @NonNull
    private int age;
    @NonNull
    private String birth;
    @NonNull
    private String address;
    @Nullable
    private String inviteCode;
    @Nullable
    private UserType type;
    @NonNull
    private Boolean isDisease;
    @Nullable
    private List<Disease> diseases;

//    public User(long userIdx, String name, String phoneNumber, int age, String birth, String address, String inviteCode, UserType type, Boolean isDisease, List<Disease> diseases) {
//        this.userIdx = userIdx;
//        this.name = name;
//        this.phoneNumber = phoneNumber;
//        this.age = age;
//        this.birth = birth;
//        this.address = address;
//        this.inviteCode = inviteCode;
//        this.type = type;
//        this.isDisease = isDisease;
//        this.diseases = diseases;
//    }
}