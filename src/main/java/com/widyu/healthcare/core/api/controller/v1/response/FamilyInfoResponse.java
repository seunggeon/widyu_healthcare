package com.widyu.healthcare.core.api.controller.v1.response;

import com.widyu.healthcare.core.api.controller.v1.response.guardian.GuardianInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.senior.SeniorInfoResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
public class FamilyInfoResponse {
    List<GuardianInfoResponse> guardianInfoResponseList;
    List<SeniorInfoResponse> seniorInfoResponseList;
    @Builder
    public FamilyInfoResponse(List<GuardianInfoResponse> guardianInfoResponseList, List<SeniorInfoResponse> seniorInfoResponseList) {
        this.guardianInfoResponseList = guardianInfoResponseList;
        this.seniorInfoResponseList = seniorInfoResponseList;
    }
}
