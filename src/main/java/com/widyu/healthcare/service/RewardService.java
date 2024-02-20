package com.widyu.healthcare.service;

import com.widyu.healthcare.dto.goals.RewardDTO;
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

    private static final String POINT_CODE_PREFIX = "point_code:";

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

        if (redisService.getPoint(buildRedisKey(userIdx.toString())) - point < 0)
            throw new InsufficientPointsException("point 부족");

        redisService.decrementPoint(userIdx.toString(), point);
        goalsStatusMapper.updateTotalPoint(userIdx, point);

        rewardMapper.updateRewardStatus(rewardIdx, 1);
        RewardDTO rewardDTO = rewardMapper.getRewardByRewardId(rewardIdx);

        return rewardDTO;
    }

    // 리워드 파일 삭제
    public void deleteReward(long rewardIdx) throws IOException {
        s3Service.deleteRewardUrl(rewardIdx);
        rewardMapper.deleteRewardByRewardIdx(rewardIdx);
    }

    private static String buildRedisKey(String userIdx) {
        return POINT_CODE_PREFIX + userIdx;
    }
}
