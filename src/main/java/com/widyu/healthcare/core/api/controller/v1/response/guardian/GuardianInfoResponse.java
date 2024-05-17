package com.widyu.healthcare.core.api.controller.v1.response.guardian;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.widyu.healthcare.core.api.controller.v1.response.CommonUserResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 아이디 찾기
 * @param name, profileImageUrl, phoneNumber, id
 * 비밀번호 찾기
 * @param name, profileImageUrl, phoneNumber, id, password
 * 모든 가족 정보 조회 attribute
 */
@Getter
@SuperBuilder
public class GuardianInfoResponse extends CommonUserResponse {
    private String id;
    @JsonIgnore
    private String password;
    private String phoneNumber;
    private String address;
    private String birth;

    public List<GuardianInfoResponse> toList() {
        List<GuardianInfoResponse> guardianDetailsList = new ArrayList<>();
        guardianDetailsList.add(this);
        return guardianDetailsList;
    }
}
