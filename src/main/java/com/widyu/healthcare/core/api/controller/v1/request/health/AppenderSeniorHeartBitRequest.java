package com.widyu.healthcare.core.api.controller.v1.request.health;

import com.widyu.healthcare.core.domain.domain.v1.Health;
import com.widyu.healthcare.core.domain.domain.v1.HealthData;
import com.widyu.healthcare.support.utils.HealthUtil;
import jakarta.annotation.Nullable;
import lombok.Getter;

/**
 *
 */
@Getter
public class AppenderSeniorHeartBitRequest {
    @Nullable
    private double heartBit;

    public HealthData toHealthData() {
        HealthData healthData = HealthData.builder()
                .data(this.heartBit)
                .status(HealthUtil.determineEmergency(this.heartBit))
                .build();
        return healthData;
    }
}
