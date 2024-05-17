package com.widyu.healthcare.core.domain.service.v1;

import com.widyu.healthcare.core.db.client.mapper.RedisMapper;
import com.widyu.healthcare.core.domain.domain.v1.Reward;
import com.widyu.healthcare.support.error.exception.InsufficientPointsException;
import com.widyu.healthcare.core.db.mapper.v1.GoalsStatusMapper;
import com.widyu.healthcare.core.db.mapper.v1.RewardsMapper;
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
    private static final String POINT_CODE_PREFIX = "point_code:";

    // 열린 리워드 전체 조회
    public List<Reward> getAllReward(Long userIdx){
        List<Reward> rewardList = rewardsMapper.getOpenedRewardByUserIdx(userIdx);
        rewardList.addAll(rewardsMapper.getClosedRewardByUserIdx(userIdx));
        return rewardList;
    }

    // 리워드 open (=구매)
    public Reward getReward(Long userIdx, Long rewardIdx) throws InsufficientPointsException {
        // 유저의 총 point redis에서 차감
        if (redisMapper.getPoint(buildRedisKey(userIdx.toString())) - REWARD_POINT < 0)
            throw new InsufficientPointsException("point 부족");

        redisMapper.decrementPoint(userIdx.toString(), REWARD_POINT);
        int updateCount = goalsStatusMapper.updateTotalPoint(userIdx, REWARD_POINT);
        if (updateCount != 1){
            log.error("update Total point ERROR! userIdx: {} total point is not updated", userIdx);
            redisMapper.decrementPoint(buildRedisKey(userIdx.toString()), REWARD_POINT);
            throw new RuntimeException("get reward ERROR! 리워드 구매 메서드를 확인해주세요\n" + "Params : userIdx:" + userIdx);
        }

        // Reward 도
        rewardsMapper.updateRewardStatus(rewardIdx, 1);
        Reward reward = rewardsMapper.getRewardByRewardId(rewardIdx);

        return reward;
    }

    // 리워드 파일 삭제
    public void deleteReward(long rewardIdx) throws IOException {
        s3Service.deleteRewardUrl(rewardIdx);
        rewardsMapper.deleteRewardByRewardIdx(rewardIdx);
    }

    private static String buildRedisKey(String userIdx) {
        return POINT_CODE_PREFIX + userIdx;
    }
}
