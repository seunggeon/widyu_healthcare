package com.widyu.healthcare.core.domain.domain.v1;

import com.widyu.healthcare.support.utils.HealthUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
@NoArgsConstructor
public class Health extends User {
    private List<HealthData> healthData;
    private double longitude;
    private double latitude;

    public double getAverageHeartBitData(List<HealthData> healthData) {
        double average = healthData.stream()
                .filter(data -> HealthType.HEARTBIT.equals(data.getHealthType()))
                .mapToDouble(HealthData::getData)
                .average()
                .orElse(0.0);
        return HealthUtil.decimalPointFirst(average);
    }
    public List<Double> getDailyDataList(List<HealthData> healthData) {
        return healthData.stream().map(HealthData::getData)
                .collect(Collectors.toList());
    }
}

