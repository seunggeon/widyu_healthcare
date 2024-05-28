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
    void updateReward(long rewardIdx, String description, RewardType type, String url);
    void updateRewardUrl(String url, long rewardIdx);
    List<RewardResponse> getRewardByUserIdxForGuardian(long userIdx);
    String getUrlByRewardIdx(long rewardIdx);
    void deleteRewardByRewardIdx(long rewardIdx);
    void updateRewardTotalStatus(long rewardIdx, Integer status);

}
