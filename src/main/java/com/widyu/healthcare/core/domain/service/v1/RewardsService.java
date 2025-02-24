package com.widyu.healthcare.core.domain.service.v1;

import com.widyu.healthcare.core.api.controller.v1.response.reward.RewardResponse;
import com.widyu.healthcare.core.domain.domain.v1.RewardType;
import com.widyu.healthcare.core.db.client.mapper.RedisMapper;
import com.widyu.healthcare.core.db.mapper.v1.RewardsStatusMapper;
import com.widyu.healthcare.core.db.mapper.v1.GoalsStatusMapper;
import com.widyu.healthcare.core.db.mapper.v1.RewardsMapper;
import com.widyu.healthcare.support.error.exception.InsufficientPointsException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.widyu.healthcare.support.config.AppConfig.REWARD_POINT;

@Log4j2
@Service
@RequiredArgsConstructor
public class RewardsService {

    private final RewardsMapper rewardsMapper;
    private final RedisMapper redisMapper;
    private final S3Service s3Service;
    private final GoalsStatusMapper goalsStatusMapper;
    private final RewardsStatusMapper rewardsStatusMapper;
    private static final String POINT_CODE_PREFIX = "point_code:";

    // 리워드 전체 조회(부양자)
    public List<RewardResponse> getAllGuardianReward(Long userIdx){
        List<RewardResponse> rewardList = rewardsMapper.getRewardByUserIdxForGuardian(userIdx);
        return rewardList;
    }

    // 리워드 전체 조회(시니어)
    public List<RewardResponse> getAllSeniorReward(Long userIdx){
        List<RewardResponse> rewardsList = rewardsStatusMapper.getRewardsIdxByUserIdx(userIdx);

        return rewardsList;
    }

    // 리워드 open (=구매)
    public void getReward(Long userIdx, Long rewardIdx) throws InsufficientPointsException {
        // 유저의 총 point redis에서 차감
        if (redisMapper.getPoint(buildRedisKey(userIdx.toString())) - REWARD_POINT < 0)
            throw new InsufficientPointsException("보유 point 부족");

        redisMapper.decrementPoint(userIdx.toString(), REWARD_POINT);
        int updateCount = goalsStatusMapper.updateTotalPoint(userIdx, -REWARD_POINT);
        if (updateCount != 1){
            log.error("goalsStatusMapper.updateTotalPoint method ERROR! userIdx: ", userIdx);
            //redisMapper.decrementPoint(buildRedisKey(userIdx.toString()), REWARD_POINT);
            throw new RuntimeException("get reward ERROR! 리워드 구매 메서드를 확인해주세요\n" + "userIdx : " + userIdx);
        }

        rewardsStatusMapper.updateRewardStatus(rewardIdx, userIdx, 1);
        rewardsMapper.updateRewardTotalStatus(rewardIdx, 1);
    }

    // 리워드 파일 삭제
    public void deleteReward(long rewardIdx) throws IOException {
        s3Service.deleteRewardUrl(rewardIdx);
        rewardsStatusMapper.deleteRewardsStatusByRewardIdx(rewardIdx);
        rewardsMapper.deleteRewardByRewardIdx(rewardIdx);
    }

    public void updateRewardInfo(long rewardIdx, String description, RewardType type){
        rewardsMapper.updateReward(rewardIdx, description, type);
    }

    private static String buildRedisKey(String userIdx) {
        return POINT_CODE_PREFIX + userIdx;
    }
}
