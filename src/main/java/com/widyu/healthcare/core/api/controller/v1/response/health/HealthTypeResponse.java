package com.widyu.healthcare.core.api.controller.v1.response.health;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.widyu.healthcare.core.api.controller.v1.response.goal.GuardianGoalResponse;
import com.widyu.healthcare.core.api.controller.v1.response.goal.SeniorGoalResponse;
import com.widyu.healthcare.core.domain.domain.v1.HealthStatus;
import com.widyu.healthcare.core.domain.domain.v1.HealthType;
import com.widyu.healthcare.support.utils.HealthUtil;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 보호자 메인 화면
 */
@Getter
public class HealthTypeResponse {
    private long userIdx;
    private String name;
    private String profileImageUrl;
    @JsonProperty("graphData")
    private List<Double> timeGraphData;
    private double dailyAverage;
    private HealthStatus compareAverage;
    private HealthType healthType;

    @Builder
    public HealthTypeResponse(long userIdx, String name, String profileImageUrl, List<Double> timeGraphData, double dailyAverage, HealthType healthType) {
        this.userIdx = userIdx;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.timeGraphData = timeGraphData;
        this.dailyAverage = dailyAverage;
        this.compareAverage = HealthUtil.compareAverages(dailyAverage);
        this.healthType = healthType;
    }
}
