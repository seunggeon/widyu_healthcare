package com.widyu.healthcare.mapper;

import com.widyu.healthcare.dto.goals.RewardDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RewardMapper {

    void insertReward(RewardDTO rewardDTO);
    void updateReward(RewardDTO rewardDTO);
    void updateRewardUrl(String url, long rewardIdx);
    void updateRewardStatus(long rewardIdx, Integer status);
    List<RewardDTO> getRewardByUserIdx(long userIdx);
    RewardDTO getRewardByRewardId(long rewardIdx);
    long getPriceByRewardIdx(long rewardIdx);
    String getUrlByRewardIdx(long rewardIdx);
    void deleteRewardByRewardIdx(long rewardIdx);
}
