package com.widyu.healthcare.core.api.controller.v1.response.health;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.widyu.healthcare.core.api.controller.v1.response.goal.GuardianGoalResponse;
import com.widyu.healthcare.core.api.controller.v1.response.goal.SeniorGoalResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 보호자 메인 화면
 */
@Getter
@NoArgsConstructor
public class SeniorMainHealthResponse {
    private long userIdx;
    private String name;
    private String profileImageUrl;
    private double latitude;
    private double longitude;
    @JsonProperty("heartBit")
    private double recentHeartBit;
    @JsonProperty("state") // 스마트 워치 최초(1회) 연동 여부
    private int watchConnection;

    @Builder
    public SeniorMainHealthResponse(long userIdx, String name, String profileImageUrl, double latitude, double longitude, double recentHeartBit, int watchConnection) {
        this.userIdx = userIdx;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.recentHeartBit = recentHeartBit;
        this.watchConnection = watchConnection;
    }
}
