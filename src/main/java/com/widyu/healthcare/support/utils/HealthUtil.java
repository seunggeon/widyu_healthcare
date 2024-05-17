package com.widyu.healthcare.support.utils;

import com.widyu.healthcare.core.domain.domain.v1.EmergencyDeterminable;
import com.widyu.healthcare.core.domain.domain.v1.HealthStatus;

import java.util.ArrayList;
import java.util.List;

import static com.widyu.healthcare.core.domain.domain.v1.HealthStatus.*;

public class HealthUtil {
    static final double criteriaOfEmergency = 95; // 심장 박동수 응급 상황 기준치
    static final double criteriaOfAverages = 95; // 심장 박동수 응급 상황 기준치


    public static HealthStatus determineEmergency(double heartBit) {
        if (heartBit > criteriaOfEmergency) {
            return Emergency;
        } else {
            return Daily;
        }
    }
    public static HealthStatus compareAverages(double dailyAverage) {
        if (dailyAverage > criteriaOfAverages) {
            return High;
        } else if (dailyAverage < criteriaOfAverages) {
            return Low;
        }
        else {
            return Normal;
        }
    }
    public static List<Double> calculateAveragesEachTime(List<Double> data) {
        List<Double> result = new ArrayList<>(12);
        int chunkSize = 120;

        double sum = 0;
        int count = 0;
        for (int i = 0; i < data.size(); i++) {
            sum += data.get(i);
            count++;
            if (count == chunkSize) {
                result.add(sum / chunkSize);
                sum = 0;
                count = 0;
            }
        }
        return result;
    }
    public static double calculateDailyAverage(List<Double> data) {
        double sum = 0.0;
        int count = data.size();

        for (double value : data) {
            sum += value;
        }

        return sum / count;
    }
    public static <T extends EmergencyDeterminable> T determineEmergency(T health) {
        health.determineFromHeartBit();
        return health;
    }
}
