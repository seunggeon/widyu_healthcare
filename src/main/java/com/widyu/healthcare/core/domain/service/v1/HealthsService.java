package com.widyu.healthcare.core.domain.service.v1;

import com.widyu.healthcare.core.api.controller.v1.response.guardian.GuardianInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.health.GuardianMainHealthResponse;
import com.widyu.healthcare.core.api.controller.v1.response.health.HealthTypeResponse;
import com.widyu.healthcare.core.api.controller.v1.response.health.SeniorDetailHealthResponse;
import com.widyu.healthcare.core.api.controller.v1.response.health.SeniorMainHealthResponse;
import com.widyu.healthcare.core.api.controller.v1.response.senior.SeniorInfoResponse;
import com.widyu.healthcare.core.db.mapper.v1.GuardiansMapper;
import com.widyu.healthcare.core.db.mapper.v1.HealthsMapper;
import com.widyu.healthcare.core.db.mapper.v1.SeniorsMapper;
import com.widyu.healthcare.core.domain.domain.v1.*;
import com.widyu.healthcare.support.utils.HealthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class HealthsService {
    private final HealthsMapper healthsMapper;
    private final SeniorsMapper seniorsMapper;
    private final GuardiansMapper guardiansMapper;

    public void insertRecentHeartBitAndStatus(long userIdx, HealthData heartBit) {
        int insertHealthCount = healthsMapper.insertRecentHeartBitAndStatus(userIdx, heartBit);
        if (insertHealthCount != 1){
            log.error("insert heartBit ERROR! info from heartBit table is null {}", heartBit);
            throw new RuntimeException("insert Goal ERROR! 심장 박동 수 추가 메서드를 확인해주세요\n" + "Params : " + heartBit);
        }

        if(heartBit.getStatus() == HealthStatus.EMERGENCY){
            // TODO: 응급 상황 푸쉬 알림
        }
    }

    public void updateRecentLocation(long userIdx, Location location) {
        int updateHealthCount = healthsMapper.updateRecentLocation(userIdx, location);
        if (updateHealthCount != 1){
            log.error("update location ERROR! info from location table is null {}", location);
            throw new RuntimeException("insert location ERROR! 사용자 위치 정보 변경 메서드를 확인해주세요\n" + "Params : " + location);
        }
    }

    public SeniorMainHealthResponse getRecentHealthOfSenior(long userIdx) {
        Health healthResponse = healthsMapper.getRecentHealth(userIdx);
        return SeniorMainHealthResponse.builder()
                .userIdx(healthResponse.getUserIdx())
                .name(healthResponse.getName())
                .profileImageUrl(healthResponse.getProfileImageUrl())
                .longitude(healthResponse.getLongitude())
                .latitude(healthResponse.getLatitude())
                .recentHeartBit(healthResponse.getAverageHeartBitData(healthResponse.getHealthData()))
                .watchConnection(1)
                .build();
    }
    public GuardianMainHealthResponse getRecentHealthOfSeniors(long targetIdx) {
        List<Long> seniorsIdxOnFamily = guardiansMapper.findSeniorsIdxByIdx(targetIdx);
        List<SeniorMainHealthResponse> seniorMainHealthResponseList = seniorsIdxOnFamily.stream()
                .map(seniorIdx -> {
                    Health healthResponse = healthsMapper.getRecentHealth(seniorIdx);
                    if (healthResponse != null) {
                        return SeniorMainHealthResponse.builder()
                                .userIdx(seniorIdx)
                                .name(healthResponse.getName())
                                .profileImageUrl(healthResponse.getProfileImageUrl())
                                .longitude(healthResponse.getLongitude())
                                .latitude(healthResponse.getLatitude())
                                .recentHeartBit(healthResponse.getAverageHeartBitData(healthResponse.getHealthData()))
                                .watchConnection(1)
                                .build();
                    }
                    return null;
                })
                .filter(response -> response != null)
                .collect(Collectors.toList());
        return GuardianMainHealthResponse.builder().seniorMainHealthResponseList(seniorMainHealthResponseList).build();
    }

    public SeniorDetailHealthResponse getSeniorDetailInfoAndHealthInfo(long userIdx) {
        SeniorInfoResponse target = seniorsMapper.findByIdx(userIdx);
        Health healthResponse = healthsMapper.getRecentHealth(userIdx);

        List<GuardianInfoResponse> guardians = seniorsMapper.findGuardiansByIdx(userIdx);
        return SeniorDetailHealthResponse.builder()
                .target(target)
                .recentHeartBit(healthResponse.getAverageHeartBitData(healthResponse.getHealthData()))
                .guardians(guardians)
                .build();
    }

    public HealthTypeResponse getDailyHearth(long userIdx, HealthType type) {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();
        Health healthResponse = healthsMapper.getDailyHealth(userIdx, month, day, String.valueOf(type));

        if(healthResponse == null) return null;

        return HealthTypeResponse.builder()
                .userIdx(healthResponse.getUserIdx())
                .name(healthResponse.getName())
                .profileImageUrl(healthResponse.getProfileImageUrl())
                .timeGraphData(HealthUtil.calculateAveragesEachTime(healthResponse.getDailyDataList(healthResponse.getHealthData())))
                .dailyAverage(HealthUtil.calculateDailyAverage(healthResponse.getDailyDataList(healthResponse.getHealthData())))
                .healthType(type)
                .build();
    }
}
