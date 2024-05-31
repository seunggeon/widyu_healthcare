package com.widyu.healthcare.core.api.controller.v1.request.senior;

import com.widyu.healthcare.core.domain.domain.v1.Disease;
import com.widyu.healthcare.core.domain.domain.v1.User;
import com.widyu.healthcare.core.domain.domain.v1.UserType;
import jakarta.annotation.Nullable;
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
public class AppendDiseaseRequest {
    @Nullable
    private String name;
    @Nullable
    private String drugName;
    @Nullable
    private String explanation;
    @Nullable
    private String dosageTime;
    @Builder
    public AppendDiseaseRequest(String name, String drugName, String explanation, String dosageTime) {
        this.name = name;
        this.drugName = drugName;
        this.explanation = explanation;
        this.dosageTime = dosageTime;
    }
    public Disease toDisease() {
        Disease disease = Disease.builder()
                .name(this.name)
                .drugName(this.drugName)
                .explanation(this.explanation)
                .dosageTime(this.dosageTime)
                .build();
        return disease;
    }
}
