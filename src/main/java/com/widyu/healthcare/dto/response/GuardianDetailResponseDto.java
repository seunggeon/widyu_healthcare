package com.widyu.healthcare.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.widyu.healthcare.utils.GoalUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
// nonnull인 것만 return
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class GuardianDetailResponseDto extends UsersResponseDto {
    private String id;
    @JsonIgnore
    private String password;
    private String phoneNumber;
    private String address;
    private String birth;

    public List<GuardianDetailResponseDto> toList() {
        List<GuardianDetailResponseDto> guardianDetailsList = new ArrayList<>();
        guardianDetailsList.add(this);
        return guardianDetailsList;
    }
}
