package com.widyu.healthcare.mapper;

import com.widyu.healthcare.dto.goals.RewardDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RewardMapper {

    void insertReward(RewardDTO rewardDTO);
    void updateReward(RewardDTO rewardDTO);
    void updateRewardUrl(String url, long rewardIdx);
    void updateRewardStatus(long rewardIdx, long status);
    List<RewardDTO> getRewardByUserId(long userIdx);
    RewardDTO getRewardByRewardId(long rewardIdx);
    long getPriceByRewardId(long rewardIdx);
    String getUrlbyRewardId(long rewardIdx);
}
