package com.widyu.healthcare.mapper;

import com.widyu.healthcare.dto.reward.RewardDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RewardMapper {

    long insertReward(RewardDTO rewardDTO);

}
