package com.widyu.healthcare.core.domain.domain.v1;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
public class Health extends User {
    private List<HealthData> healthData;
    private List<Double> DailyDataList;
    private double longitude;
    private double latitude;

    public double getHeartBitData() {
        return this.healthData.stream()
                .filter(data -> "heartBit".equals(data.getType()))
                .mapToDouble(HealthData::getData)
                .average()
                .orElse(0.0);
    }
}

