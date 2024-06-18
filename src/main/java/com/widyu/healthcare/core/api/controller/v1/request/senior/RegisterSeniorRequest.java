package com.widyu.healthcare.core.api.controller.v1.request.senior;

import com.widyu.healthcare.core.domain.domain.v1.Disease;
import com.widyu.healthcare.core.domain.domain.v1.User;
import com.widyu.healthcare.core.domain.domain.v1.UserType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import java.util.List;

import static com.widyu.healthcare.core.domain.domain.v1.UserType.SENIOR;
import static com.widyu.healthcare.support.utils.UuidUtil.generateUniqueID;

/**
 * 시니어 회원가입
 * 시니어 추가 등록
 * @param
 */
@Getter
public class RegisterSeniorRequest {
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
    @NotBlank(message = "isDisease cannot be blank")
    private Boolean isDisease;
    @Nullable
    private List<Disease> diseases;
    @Builder
    public RegisterSeniorRequest(long userIdx, String name, String phoneNumber, int age, String birth, String address, String inviteCode, UserType type, Boolean isDisease, List<Disease> diseases) {
        this.userIdx = userIdx;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.birth = birth;
        this.address = address;
        this.inviteCode = inviteCode;
        this.type = type;
        this.isDisease = isDisease;
        this.diseases = diseases;
    }
    public User toEncryptedUser() {
        User user = User.builder()
                .userIdx(this.userIdx)
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .age(this.age)
                .birth(this.birth)
                .address(this.address)
                .inviteCode(generateUniqueID())
                .type(SENIOR)
                .isDisease(this.isDisease)
                .diseases(this.diseases)
                .build();
        return user;
    }
}
