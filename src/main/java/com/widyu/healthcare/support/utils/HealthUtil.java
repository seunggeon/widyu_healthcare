package com.widyu.healthcare.support.utils;

import com.widyu.healthcare.core.domain.domain.v1.EmergencyDeterminable;
import com.widyu.healthcare.core.domain.domain.v1.HealthStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.widyu.healthcare.core.domain.domain.v1.HealthStatus.*;

public class HealthUtil {
    static final double criteriaOfEmergency = 95; // 심장 박동수 응급 상황 기준치
    static final double criteriaOfAverages = 95; // 심장 박동수 평균 기준치


    public static HealthStatus determineEmergency(double heartBit) {
        if (heartBit > criteriaOfEmergency) {
            return EMERGENCY;
        } else if(heartBit > criteriaOfEmergency) {
            return DAILY;
        }
        else {
            return WELL;
        }
    }
    public static HealthStatus compareAverages(double dailyAverage) {
        if (dailyAverage > criteriaOfAverages) {
            return HIGH;
        } else if (dailyAverage < criteriaOfAverages) {
            return LOW;
        }
        else {
            return NORMAL;
        }
    }
    public static List<Double> calculateAveragesEachTime(List<Double> input) {
        List<Double> result = new ArrayList<>(24);
        int chunkSize = 60;
        int maxChunks = 24;
        double sum = 0;
        int count = 0;
        double average = 0;

        for (int i = 0; i < input.size(); i++) {
            sum += input.get(i);
            count++;
            // 60분씩 평균
            if (count == chunkSize) {
                average = sum / chunkSize;
                result.add(HealthUtil.decimalPointFirst(average));
                sum = 0;
                count = 0;
                average = 0.0;
            }
            // 24시간 채워지면 Break
            if (result.size() == maxChunks) {
                break;
            }
        }
        // chunkSize보다 적은 chunk 데이터 평균
        if (count > 0 && result.size() < maxChunks) {
            average = sum / chunkSize;
            result.add(HealthUtil.decimalPointFirst(average));
        }

        return result;
    }
    public static double calculateDailyAverage(List<Double> data) {
        double sum = 0.0;
        double average = 0.0;
        int count = data.size();

        for (double value : data) {
            sum += value;
        }

        average = sum / count;
        return HealthUtil.decimalPointFirst(average);
    }

    public static double decimalPointFirst(double data) {
        return Math.round(data * 10) / 10.0;
    }

    public static <T extends EmergencyDeterminable> T determineEmergency(T health) {
        health.determineFromHeartBit();
        return health;
    }
}
