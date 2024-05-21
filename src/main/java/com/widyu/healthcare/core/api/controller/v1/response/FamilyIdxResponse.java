package com.widyu.healthcare.core.api.controller.v1.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.widyu.healthcare.core.api.controller.v1.response.guardian.GuardianInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.senior.SeniorInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class FamilyIdxResponse {
    List<Long> seniorsIdxList;
    List<Long> guardiansIdxList;
    @Builder
    public FamilyIdxResponse(List<Long> seniorsIdxList, List<Long> guardiansIdxList) {
        this.seniorsIdxList = seniorsIdxList;
        this.guardiansIdxList = guardiansIdxList;
    }
}
