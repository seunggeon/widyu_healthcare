package com.widyu.healthcare.core.domain.domain.v1;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import okhttp3.internal.http2.Http2Reader;

import java.util.List;

@Getter
@NoArgsConstructor
public class HealthData {
    private long healthIdx;
    private double data;
    private HealthStatus status;
    private HealthType healthType;

    @Builder
    public HealthData(long healthIdx, double data, HealthStatus status, HealthType healthType) {
        this.healthIdx = healthIdx;
        this.data = data;
        this.status = status;
        this.healthType = healthType;
    }
}
