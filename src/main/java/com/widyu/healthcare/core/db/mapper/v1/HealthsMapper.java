package com.widyu.healthcare.core.db.mapper.v1;

import com.widyu.healthcare.core.domain.domain.v1.Health;
import com.widyu.healthcare.core.domain.domain.v1.HealthData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HealthsMapper {
    int insertRecentHeartBitAndStatus(long userIdx,@Param("health") HealthData recentHeartBit);
    Health getRecentHealth(long userIdx);
    Health getDailyHealth(long userIdx, int month, int day, String healthType);
}
