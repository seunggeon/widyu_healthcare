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
    private double data;
    private HealthStatus status;
    private HealthType type;

    @Builder
    public HealthData(double data, HealthStatus status, HealthType type) {
        this.data = data;
        this.status = status;
        this.type = type;
    }
}
