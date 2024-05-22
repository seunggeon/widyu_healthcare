package com.widyu.healthcare.core.domain.domain.v1;

public enum HealthStatus {
    WELL("좋음"),
    DAILY("보통"),
    EMERGENCY("응급 상황"),
    HIGH("평균 이상"),
    NORMAL("평균"),
    LOW("평균 이하");
    private String healthStatus;
    HealthStatus(String HealthStatus) {
        this.healthStatus = HealthStatus;
    }

    public String getHealthStatus() {
        return healthStatus;
    }
}
