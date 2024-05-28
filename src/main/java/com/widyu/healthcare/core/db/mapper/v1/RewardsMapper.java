package com.widyu.healthcare.core.db.mapper.v1;

import com.widyu.healthcare.core.api.controller.v1.response.reward.RewardResponse;
import com.widyu.healthcare.core.domain.domain.v1.Reward;
import com.widyu.healthcare.core.domain.domain.v1.RewardType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RewardsMapper {

    void insertReward(Reward reward);
    void insertRewardStatus(Reward reward);
    void updateReward(long rewardIdx, String description, RewardType type, String url);
    void updateRewardUrl(String url, long rewardIdx);
    List<RewardResponse> getOpenedRewardByUserIdxForSenior(long userIdx);
    RewardResponse getOpenedRewardByRewardIdxForSenior(long rewardIdx);
    List<RewardResponse> getRewardByUserIdxForGuardian(long userIdx);
    List<RewardResponse> getClosedRewardByUserIdxForSenior(long userIdx);
    Reward getRewardByRewardId(long rewardIdx);
    long getPriceByRewardIdx(long rewardIdx);
    String getUrlByRewardIdx(long rewardIdx);
    void deleteRewardByRewardIdx(long rewardIdx);
}
