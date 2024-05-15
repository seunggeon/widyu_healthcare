package com.widyu.healthcare.core.db.mapper.v1;

import com.widyu.healthcare.core.domain.domain.v1.Reward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RewardsMapper {

    void insertReward(Reward reward);
    void updateReward(Reward reward);
    void updateRewardUrl(String url, long rewardIdx);
    void updateRewardStatus(long rewardIdx, Integer status);
    List<Reward> getOpenedRewardByUserIdx(long userIdx);
    List<Reward> getClosedRewardByUserIdx(long userIdx);
    List<Reward> getClosedRewardInfoByUserIdx(long userIdx);
    Reward getRewardByRewardId(long rewardIdx);
    long getPriceByRewardIdx(long rewardIdx);
    String getUrlByRewardIdx(long rewardIdx);
    void deleteRewardByRewardIdx(long rewardIdx);

    long getRewardDaily(long userIdx, int day);
    Map<Integer, Double> getRewardMonthly(long userIdx, int month);
}
