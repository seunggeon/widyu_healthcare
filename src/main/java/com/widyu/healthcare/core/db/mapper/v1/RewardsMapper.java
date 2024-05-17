package com.widyu.healthcare.core.db.mapper.v1;

import com.widyu.healthcare.core.api.controller.v1.response.reward.RewardResponse;
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
    List<RewardResponse> getOpenedRewardByUserIdx(long userIdx);
    List<RewardResponse> getClosedRewardByUserIdx(long userIdx);
    List<RewardResponse> getClosedRewardByUserIdxForSenior(long userIdx);
    Reward getRewardByRewardId(long rewardIdx);
    long getPriceByRewardIdx(long rewardIdx);
    String getUrlByRewardIdx(long rewardIdx);
    void deleteRewardByRewardIdx(long rewardIdx);

    long getRewardRateDaily(long userIdx, int month, int day);
    List<Map<Integer, Double>> getRewardRateMonthly(long userIdx, int month);
}
