package com.widyu.healthcare.mapper;

import com.widyu.healthcare.dto.reward.RewardDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RewardMapper {

    void insertReward(RewardDTO rewardDTO);
    void updateReward(RewardDTO rewardDTO);
    void updateRewardUrl(String url, long rewardIdx);
    String getUrlbyRewardId(long rewardIdx);
}
