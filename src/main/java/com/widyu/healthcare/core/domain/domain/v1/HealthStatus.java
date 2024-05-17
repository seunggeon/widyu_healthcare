package com.widyu.healthcare.core.domain.domain.v1;

public enum HealthStatus {
    Well("좋음"),
    Daily("보통"),
    Emergency("응급 상황"),
    High("평균 이상"),
    Normal("평균"),
    Low("평균 이하");
    private String healthStatus;
    HealthStatus(String HealthStatus) {
        this.healthStatus = HealthStatus;
    }

    public String getHealthStatus() {
        return healthStatus;
    }
}
