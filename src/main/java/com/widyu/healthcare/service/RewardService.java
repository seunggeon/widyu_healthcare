package com.widyu.healthcare.service;

import com.widyu.healthcare.dto.reward.RewardDTO;
import com.widyu.healthcare.error.exception.InsufficientPointsException;
import com.widyu.healthcare.mapper.GoalsStatusMapper;
import com.widyu.healthcare.mapper.RewardMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Log4j2
@Service
public class RewardService {


    private final RewardMapper rewardMapper;
    private final RedisService redisService;
    private final S3Service s3Service;
    private final GoalsStatusMapper goalsStatusMapper;

    public RewardService(RewardMapper rewardMapper, RedisService redisService, S3Service s3Service, GoalsStatusMapper goalsStatusMapper) {
        this.rewardMapper = rewardMapper;
        this.redisService = redisService;
        this.s3Service = s3Service;
        this.goalsStatusMapper = goalsStatusMapper;
    }

    // 리워드 전체 조회
    public List<RewardDTO> getAllReward(Long userIdx){
        return rewardMapper.getRewardByUserIdx(userIdx);
    }

    // 리워드 조회 (=구매)
    public RewardDTO getReward(Long userIdx, Long rewardIdx) throws InsufficientPointsException {
        long point = rewardMapper.getPriceByRewardIdx(rewardIdx);

        log.info("[RP] redis point: {}/ img point: {}", redisService.getPoint(userIdx.toString()), point);
        if (redisService.getPoint(userIdx.toString()) - point < 0)
            throw new InsufficientPointsException("point 부족");

        redisService.decrementPoint(userIdx.toString(), point);
        goalsStatusMapper.updateTotalPoint(userIdx, point);

        RewardDTO rewardDTO = rewardMapper.getRewardByRewardId(rewardIdx);
        rewardMapper.updateRewardStatus(rewardIdx, 1L);
        return rewardDTO;
    }

    // 리워드 파일 삭제
    public void deleteReward(long rewardIdx) throws IOException {
        s3Service.deleteRewardUrl(rewardIdx);
        rewardMapper.deleteRewardByRewardIdx(rewardIdx);
    }

}
