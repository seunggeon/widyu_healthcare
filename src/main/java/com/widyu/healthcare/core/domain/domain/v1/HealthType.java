package com.widyu.healthcare.core.domain.domain.v1;

public enum HealthType {
    OXYGENSATURATION("산소 포화도"),
    TEMPERATURE("체온"),
    HEARTBIT("심장 박동수");
    private String healthType;
    HealthType(String healthType) {
        this.healthType = healthType;
    }

    public String getHealthType() {
        return healthType;
    }
}
