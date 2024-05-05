package com.widyu.healthcare.service;

import com.widyu.healthcare.dto.domain.RewardDto;
import com.widyu.healthcare.error.exception.InsufficientPointsException;
import com.widyu.healthcare.mapper.GoalsStatusMapper;
import com.widyu.healthcare.mapper.RewardMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.widyu.healthcare.config.AppConfig.GOAL_POINT;
import static com.widyu.healthcare.config.AppConfig.REWARD_POINT;

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

    // 열린 리워드 전체 조회
    public List<RewardDto> getAllReward(Long userIdx){
        List<RewardDto> rewardDtoList = rewardMapper.getOpenedRewardByUserIdx(userIdx);
        rewardDtoList.addAll(rewardMapper.getClosedRewardByUserIdx(userIdx));
        return rewardDtoList;
    }

    // 리워드 open (=구매)
    public RewardDto getReward(Long userIdx, Long rewardIdx) throws InsufficientPointsException {

        if (redisService.getPoint(buildRedisKey(userIdx.toString())) - REWARD_POINT < 0)
            throw new InsufficientPointsException("point 부족");

        redisService.decrementPoint(userIdx.toString(), REWARD_POINT);
        int updateCount = goalsStatusMapper.updateTotalPoint(userIdx, REWARD_POINT);
        if (updateCount != 1){
            log.error("update Total point ERROR! userIdx: {} total point is not updated", userIdx);
            redisService.decrementPoint(buildRedisKey(userIdx.toString()), REWARD_POINT);
            throw new RuntimeException("get reward ERROR! 리워드 구매 메서드를 확인해주세요\n" + "Params : userIdx:" + userIdx);
        }

        rewardMapper.updateRewardStatus(rewardIdx, 1);
        RewardDto rewardDTO = rewardMapper.getRewardByRewardId(rewardIdx);

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
