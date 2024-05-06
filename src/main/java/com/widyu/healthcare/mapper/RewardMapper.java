package com.widyu.healthcare.mapper;

import com.widyu.healthcare.dto.domain.RewardDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RewardMapper {

    void insertReward(RewardDto rewardDTO);
    void updateReward(RewardDto rewardDTO);
    void updateRewardUrl(String url, long rewardIdx);
    void updateRewardStatus(long rewardIdx, Integer status);
    List<RewardDto> getOpenedRewardByUserIdx(long userIdx);
    List<RewardDto> getClosedRewardByUserIdx(long userIdx);
    RewardDto getRewardByRewardId(long rewardIdx);
    long getPriceByRewardIdx(long rewardIdx);
    String getUrlByRewardIdx(long rewardIdx);
    void deleteRewardByRewardIdx(long rewardIdx);
}
