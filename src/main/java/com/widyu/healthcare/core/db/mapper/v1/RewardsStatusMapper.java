package com.widyu.healthcare.core.db.mapper.v1;

import com.widyu.healthcare.core.api.controller.v1.response.reward.RewardResponse;
import com.widyu.healthcare.core.domain.domain.v1.Reward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RewardsStatusMapper {

    void insertRewardStatus(Reward reward);
    List<RewardResponse> getRewardsIdxByUserIdx(long userIdx);
    void updateRewardStatus(long rewardIdx, Integer status);

}
