package com.widyu.healthcare.core.api.controller.v1.response.health;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.widyu.healthcare.core.api.controller.v1.response.goal.GuardianGoalResponse;
import com.widyu.healthcare.core.api.controller.v1.response.goal.SeniorGoalResponse;
import com.widyu.healthcare.core.api.controller.v1.response.guardian.GuardianInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.senior.SeniorInfoResponse;
import com.widyu.healthcare.core.domain.domain.v1.Health;
import com.widyu.healthcare.core.domain.domain.v1.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 보호자 메인 화면
 */
@Getter
public class SeniorDetailHealthResponse {
    SeniorInfoResponse target;
//    @JsonIgnore
//    private long userIdx;
//    private String name;
//    private String profileImageUrl;
//    // phoneNumber
//    // address
//    // birth
//    // disease
//    // drugName
//    // favoriteHospital
//    // explanation
    @JsonProperty("heartBit")
    private double recentHeartBit;
//    @JsonProperty("o2")
//    private double recentOxygenSaturation;
//    @JsonProperty("temperature")
//    private double recentTemperature;

    List<GuardianInfoResponse> guardians;
    // guardian Name
    // guardian phoneNumber
    // guardian address

    @Builder
    public SeniorDetailHealthResponse(SeniorInfoResponse target, double recentHeartBit, List<GuardianInfoResponse> guardians) {
        this.target = target;
        this.recentHeartBit = recentHeartBit;
        this.guardians = guardians;
    }
}
