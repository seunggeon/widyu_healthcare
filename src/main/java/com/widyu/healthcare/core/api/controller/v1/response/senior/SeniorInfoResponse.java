package com.widyu.healthcare.core.api.controller.v1.response.senior;

import java.util.ArrayList;
import java.util.List;
import com.widyu.healthcare.core.api.controller.v1.response.CommonUserResponse;
import com.widyu.healthcare.core.domain.domain.v1.Disease;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 모든 가족 정보 조회 attribute
 */
@Getter
@SuperBuilder
public class SeniorInfoResponse extends CommonUserResponse {
    private String inviteCode;
    private String phoneNumber;
    private String address;
    private String birth;
    private int isDisease;

    private List<Disease> diseases;
    public List<SeniorInfoResponse> toList() {
        List<SeniorInfoResponse> seniorDetailsList = new ArrayList<>();
        seniorDetailsList.add(this);
        return seniorDetailsList;
    }
}
