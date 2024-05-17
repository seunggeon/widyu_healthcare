package com.widyu.healthcare.core.domain.service.v1;

import com.widyu.healthcare.core.api.controller.v1.response.reward.RewardResponse;
import com.widyu.healthcare.core.db.client.mapper.RedisMapper;
import com.widyu.healthcare.core.domain.domain.v1.Reward;
import com.widyu.healthcare.support.error.exception.InsufficientPointsException;
import com.widyu.healthcare.core.db.mapper.v1.GoalsStatusMapper;
import com.widyu.healthcare.core.db.mapper.v1.RewardsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    // 리워드 전체 조회(부양자)
    public List<RewardResponse> getAllGurdianReward(Long userIdx){
        List<RewardResponse> rewardList = rewardsMapper.getOpenedRewardByUserIdx(userIdx);
        rewardList.addAll(rewardsMapper.getClosedRewardByUserIdx(userIdx));
        return rewardList;
    }

    // 리워드 전체 조회(시니어)
    public List<RewardResponse> getAllSeniorReward(Long userIdx){
        List<RewardResponse> rewardList = rewardsMapper.getOpenedRewardByUserIdx(userIdx);
        rewardList.addAll(rewardsMapper.getClosedRewardByUserIdxForSenior(userIdx));
        return rewardList;
    }

    // 리워드 open (=구매)
    public Reward getReward(Long userIdx, Long rewardIdx) throws InsufficientPointsException {

        if (redisMapper.getPoint(buildRedisKey(userIdx.toString())) - REWARD_POINT < 0)
            throw new InsufficientPointsException("point 부족");

        redisMapper.decrementPoint(userIdx.toString(), REWARD_POINT);
        int updateCount = goalsStatusMapper.updateTotalPoint(userIdx, REWARD_POINT);
        if (updateCount != 1){
            log.error("update Total point ERROR! userIdx: {} total point is not updated", userIdx);
            redisMapper.decrementPoint(buildRedisKey(userIdx.toString()), REWARD_POINT);
            throw new RuntimeException("get reward ERROR! 리워드 구매 메서드를 확인해주세요\n" + "Params : userIdx:" + userIdx);
        }

        rewardsMapper.updateRewardStatus(rewardIdx, 1);
        Reward reward = rewardsMapper.getRewardByRewardId(rewardIdx);

        return reward;
    }

    // 리워드 파일 삭제
    public void deleteReward(long rewardIdx) throws IOException {
        s3Service.deleteRewardUrl(rewardIdx);
        rewardsMapper.deleteRewardByRewardIdx(rewardIdx);
    }

    // 오늘 리워드 달성률 조회
    public long getRewardRateToday(long userIdx){
        // 오늘 날짜 가져오기
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        return rewardsMapper.getRewardRateDaily(userIdx, month, day);
    }

    // 월별 리워드 달성률 조회
    public List<Map<Integer, Double>> getRewardRateMontly(long userIdx, int month){

        return rewardsMapper.getRewardRateMonthly(userIdx, month);
    }

    private static String buildRedisKey(String userIdx) {
        return POINT_CODE_PREFIX + userIdx;
    }
}
