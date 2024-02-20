package com.widyu.healthcare.service;

import com.widyu.healthcare.dto.goals.RewardDTO;
import com.widyu.healthcare.error.exception.InsufficientPointsException;
import com.widyu.healthcare.mapper.GoalsStatusMapper;
import com.widyu.healthcare.mapper.RewardMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class RewardService {


    private final RewardMapper rewardMapper;
    private final RedisService redisService;
    private final GoalsStatusMapper goalsStatusMapper;

    public RewardService(RewardMapper rewardMapper, RedisService redisService, GoalsStatusMapper goalsStatusMapper) {
        this.rewardMapper = rewardMapper;
        this.redisService = redisService;
        this.goalsStatusMapper = goalsStatusMapper;
    }

    // 목표 전체 조회
    public List<RewardDTO> getAllReward(Long userIdx){
        return rewardMapper.getRewardByUserId(userIdx);
    }

    // 목표 조회 (=구매)
    public RewardDTO getReward(Long userIdx, Long rewardIdx) throws InsufficientPointsException {
        long point = rewardMapper.getPriceByRewardId(rewardIdx);

        if (redisService.getPoint(userIdx.toString()) - point < 0)
            throw new InsufficientPointsException("point 부족");

        redisService.decrementPoint(userIdx.toString(), point);
        goalsStatusMapper.updateTotalPoint(userIdx, point);

        RewardDTO rewardDTO = rewardMapper.getRewardByRewardId(rewardIdx);
        rewardMapper.updateRewardStatus(rewardIdx, 1L);
        return rewardDTO;
    }

}
