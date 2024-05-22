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
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 보호자 메인 화면
 */
@Getter
@NoArgsConstructor
public class SeniorDetailHealthResponse {
    @JsonProperty("ResponseOldHealthDto")
    SeniorInfoResponse target;
    @JsonProperty("heartBit")
    private double recentHeartBit;
    @JsonProperty("ResponseYoungInfoDto")
    List<GuardianInfoResponse> guardians;

    @Builder
    public SeniorDetailHealthResponse(SeniorInfoResponse target, double recentHeartBit, List<GuardianInfoResponse> guardians) {
        this.target = target;
        this.recentHeartBit = recentHeartBit;
        this.guardians = guardians;
    }
}
