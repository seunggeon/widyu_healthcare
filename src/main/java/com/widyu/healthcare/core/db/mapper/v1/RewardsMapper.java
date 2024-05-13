package com.widyu.healthcare.core.db.mapper.v1;

import com.widyu.healthcare.core.domain.domain.v1.Reward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RewardsMapper {

    void insertReward(Reward reward);
    void updateReward(Reward reward);
    void updateRewardUrl(String url, long rewardIdx);
    void updateRewardStatus(long rewardIdx, Integer status);
    List<Reward> getOpenedRewardByUserIdx(long userIdx);
    List<Reward> getClosedRewardByUserIdx(long userIdx);
    Reward getRewardByRewardId(long rewardIdx);
    long getPriceByRewardIdx(long rewardIdx);
    String getUrlByRewardIdx(long rewardIdx);
    void deleteRewardByRewardIdx(long rewardIdx);
}
