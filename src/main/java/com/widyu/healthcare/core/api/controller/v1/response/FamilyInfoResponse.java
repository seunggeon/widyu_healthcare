package com.widyu.healthcare.core.api.controller.v1.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.widyu.healthcare.core.api.controller.v1.response.guardian.GuardianInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.senior.SeniorInfoResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
public class FamilyInfoResponse<T> {
    @JsonProperty("apiUserResponseDto")
    T targetInfoResponse;
    @JsonProperty("guardianDetailResponseDtos")
    List<GuardianInfoResponse> guardianInfoResponseList;
    @JsonProperty("seniorDetailResponseDtos")
    List<SeniorInfoResponse> seniorInfoResponseList;
    @Builder
    public FamilyInfoResponse(T targetInfoResponse, List<GuardianInfoResponse> guardianInfoResponseList, List<SeniorInfoResponse> seniorInfoResponseList) {
        this.targetInfoResponse = targetInfoResponse;
        this.guardianInfoResponseList = guardianInfoResponseList;
        this.seniorInfoResponseList = seniorInfoResponseList;
    }
}
