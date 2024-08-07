package com.widyu.healthcare.support.utils;

import com.widyu.healthcare.core.api.controller.v1.response.guardian.GuardianInfoResponse;
import com.widyu.healthcare.core.db.mapper.v1.SeniorsMapper;
import com.widyu.healthcare.core.domain.domain.v1.EmergencyDeterminable;
import com.widyu.healthcare.core.domain.domain.v1.HealthStatus;
import com.widyu.healthcare.core.domain.service.v1.FcmService;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.widyu.healthcare.core.domain.domain.v1.HealthStatus.*;

@Component
public class HealthUtil {
    static final double criteriaOfEmergency = 95; // 심장 박동수 응급 상황 기준치
    static final double criteriaOfAverages = 95; // 심장 박동수 평균 기준치
    private static FcmService fcmService;
    private static SeniorsMapper seniorsMapper;

    public HealthUtil(FcmService fcmService, SeniorsMapper seniorsMapper) {
        HealthUtil.fcmService = fcmService;
        HealthUtil.seniorsMapper = seniorsMapper;
    }

    public static HealthStatus determineEmergency(double heartBit, long seniorIdx) {

        if (heartBit > criteriaOfEmergency) {
            try {
                String username = seniorsMapper.findDetailByIdx(seniorIdx).getName();
                fcmService.sendMessage(seniorsMapper.findFCM(seniorIdx), FcmService.Situation.HEALTH_ALERT, username, null);
                fcmService.sendMessageToGuardians(seniorIdx, FcmService.Situation.HEALTH_ALERT, username, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return EMERGENCY;
        } else if(heartBit == criteriaOfEmergency) {
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
    @Nullable
    public static List<Double> calculateAveragesEachTime(List<Double> input) {

        if(input.isEmpty()) return null;

        List<Double> result = new ArrayList<>(24);
        // 1분에 센서 데이터 하나씩 전송
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
        // 60분이 다 채워지지 않은 데이터의 평균
        if (count > 0 && result.size() < maxChunks) {
            average = sum / chunkSize;
            result.add(HealthUtil.decimalPointFirst(average));
        }

        // 빈 시간대는 0으로 채우기
        while (result.size() < maxChunks) {
            result.add(0.0);
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
