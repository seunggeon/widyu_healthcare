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