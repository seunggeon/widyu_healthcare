package com.widyu.healthcare.core.api.controller.v1.response.goal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MainGoalResponse {
    @JsonProperty("guardianGoalList")
    GuardianGoalResponse guardianGoalResponse;
    @JsonProperty("seniorsGoalList")
    List<SeniorGoalResponse> seniorGoalResponseList;
    @Builder
    public MainGoalResponse(GuardianGoalResponse guardianGoalResponse, List<SeniorGoalResponse> seniorGoalResponseList) {
        this.guardianGoalResponse = guardianGoalResponse;
        this.seniorGoalResponseList = seniorGoalResponseList;
    }
}