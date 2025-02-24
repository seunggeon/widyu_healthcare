package com.widyu.healthcare.core.domain.service.v1;

import com.widyu.healthcare.core.api.controller.v1.response.guardian.GuardianInfoResponse;
import com.widyu.healthcare.core.db.client.mapper.RedisMapper;
import com.widyu.healthcare.core.domain.domain.v1.Goal;
import com.widyu.healthcare.core.api.controller.v1.response.goal.GuardianGoalResponse;
import com.widyu.healthcare.core.api.controller.v1.response.goal.SeniorGoalResponse;
import com.widyu.healthcare.core.api.controller.v1.response.goal.MainGoalResponse;
import com.widyu.healthcare.core.domain.domain.v1.GoalStatus;
import com.widyu.healthcare.support.jobs.GoalAlarmJob;
import com.widyu.healthcare.support.jobs.StatusJob;
import com.widyu.healthcare.core.db.mapper.v1.GoalsStatusMapper;
import com.widyu.healthcare.core.db.mapper.v1.GoalsMapper;
import com.widyu.healthcare.core.db.mapper.v1.GuardiansMapper;
import com.widyu.healthcare.core.db.mapper.v1.SeniorsMapper;

import com.widyu.healthcare.support.utils.GoalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import org.quartz.DateBuilder;
import static org.quartz.DateBuilder.*;
import java.time.ZoneId;
import java.util.*;
import java.util.Calendar;
import java.util.stream.Collectors;

import static com.widyu.healthcare.support.config.AppConfig.GOAL_POINT;


@Log4j2
@Service
@RequiredArgsConstructor
public class GoalsService {
    private final GoalsMapper goalsMapper;
    private final SeniorsMapper seniorsMapper;
    private final GuardiansMapper guardiansMapper;
    private final GoalsStatusMapper goalsStatusMapper;
    private final RedisMapper redisMapper;
    private final FcmService fcmService;
    private final Scheduler scheduler;
    private static final String POINT_CODE_PREFIX = "point_code:";
    public MainGoalResponse getTargetUserGoalsAndSeniorGoals(long targetIdx){

        GuardianGoalResponse myGoal = goalsMapper.findByGuardianIdx(targetIdx);

        // 가족들(시니어) 목표 정보
        List<Long> seniorsIdxOnFamily = guardiansMapper.findSeniorsIdxByIdx(targetIdx);
        List<SeniorGoalResponse> seniorsGoalList;
        seniorsGoalList = seniorsIdxOnFamily.stream()
                .map(userIdx -> goalsMapper.findBySeniorIdx(userIdx))
                .peek(dto -> dto.percentageFromGoals())
                .collect(Collectors.toList());

        // 본인과 가족들 목표 정보
        MainGoalResponse mainGoals = MainGoalResponse.builder()
                .guardianGoalResponse(myGoal)
                .seniorGoalResponseList(seniorsGoalList)
                .build();

        return mainGoals;
    }

    public SeniorGoalResponse getTargetSeniorGoals(long targetIdx){

        // 본인(시니어) 목표 정보
        SeniorGoalResponse myGoal = goalsMapper.findBySeniorIdx(targetIdx);

        return myGoal;
    }

    // 목표 조회
    public List<Goal> getGoal(long userIdx){

        List<Goal> goalList = goalsMapper.getGoalsByUserIdx(userIdx);

        return goalList;
    }

    // 목표 생성
    public Goal insertGoal(Goal goal, List<GoalStatus> goalStatusList){

        int insertGoalCount = goalsMapper.insertGoal(goal);
        if (insertGoalCount != 1){
            log.error("goalsMapper.insertGoal method ERROR! goalIdx : ", goal.getGoalIdx());
            throw new RuntimeException("insert Goal ERROR! 목표 생성 메서드를 확인해주세요\n" + " goalIdx : " + goal.getGoalIdx());
        }

        // 현재 요일 구하기
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        try {
            goalStatusList.forEach(goalStatus -> {
                goalStatus.setGoalIdx(goal.getGoalIdx());
                if (goal.getDay().toCharArray()[dayOfWeek - 1] == '1')
                    goalsStatusMapper.insertGoalStatus(goalStatus);
                // 목표 관련 스케줄러
                // 1. 하루 지났을 때 수행 안한 목표는 실패
                // 2. 목표 실행 시간 시 알림
                long userIdx = goal.getUserIdx();
                scheduleTimerForGoalStatus(goalStatus, userIdx, seniorsMapper.findDetailByIdx(userIdx).getName());
            });
        } catch (RuntimeException e) {
            log.error("goalStatus scheduler ERROR!", e);
            throw new RuntimeException("goalStatus scheduler ERROR! 하루 지났을 때 수행 안한 목표는 실패로 만드는 스케줄러를 확인해주세요\n");
        }
        return goal;
    }

    // 목표 수정
    public void updateGoal(Goal goal, List<GoalStatus> goalStatusList){


        int updateGoalCount = goalsMapper.updateGoal(goal);
        if (updateGoalCount != 1){
            log.error("goalsMapper.updateGoal method ERROR! goalIdx : ", goal.getGoalIdx());
            throw new RuntimeException("update Goal ERROR! 목표 수정 메서드를 확인해주세요\n");
        }

        goalStatusList.forEach(goalStatus -> {goalsStatusMapper.updateGoalStatus(goalStatus);});
    }

    // 목표 삭제
    public void deleteGoal(long goalIdx){

        goalsStatusMapper.deleteGoalStatus(goalIdx);

        int deleteCount = goalsMapper.deleteGoal(goalIdx);
        if (deleteCount != 1){
            log.error("goalsMapper.deleteGoal method ERROR! goalIdx : ", goalIdx);
            throw new RuntimeException("delete Goal ERROR! 목표 삭제 메서드를 확인해주세요\n" + "goalIdx: " + goalIdx);
        }
    }

    // 목표 상태 수정 (성공)
    public void updateStatusSuccess(Long userIdx, Long goalStatusIdx) throws IOException {
        byte success = 1;

        // 목표 상태 수정 -> 성공
        int updateStatusCount = goalsStatusMapper.updateStatus(goalStatusIdx, success);
        if (updateStatusCount != 1){
            log.error("goalsStatusMapper.updateStatus method ERROR! goalStatusIdx: {} status is not updated", goalStatusIdx);
            throw new RuntimeException("delete Goal ERROR! 목표 상태 수정 메서드를 확인해주세요\n" + "Params : userIdx:" + userIdx);
        }

        // 목표 달성 포인트 임시 저장
        redisMapper.incrementPoint(buildRedisKey(userIdx.toString()), GOAL_POINT);

        // 총 포인트 변경
        try {
            int updateCount = goalsStatusMapper.updateTotalPoint(userIdx, GOAL_POINT);
            if (updateCount != 1) {
                log.error("update Total point ERROR! userIdx: {} total point is not updated", userIdx);
                throw new RuntimeException("update status success ERROR! 목표 상태 수정(성공) 메서드를 확인해주세요\n" + "Params : userIdx:" + userIdx);
            }
        } catch (RuntimeException e){
            redisMapper.decrementPoint(buildRedisKey(userIdx.toString()), GOAL_POINT);
            log.error("goalsStatusMapper.updateTotalPoint method ERROR! goalStatusIdx: {} status is not updated", goalStatusIdx);
            throw new RuntimeException("update status success ERROR! 목표 상태 수정(성공) 메서드를 확인해주세요\n" + "userIdx :" + userIdx);
        }

        long goalIdx = goalsStatusMapper.getGoalStatusByGoalStatusIdx(goalStatusIdx).getGoalIdx();
        // 푸쉬 알림(senior): 시니어 본인에게 성공 알림
        fcmService.sendMessage(seniorsMapper.findFCM(userIdx), FcmService.Situation.GOAL_COMPLETION, seniorsMapper.findDetailByIdx(userIdx).getName(), goalsMapper.getGoalByGoalIdx(goalIdx).getTitle());
        // 푸쉬 알림(guardian): 시니어가 하루의 모든 목표 달성 시 보조자에게 성공 알림
        fcmService.sendMessageToGuardians(userIdx, FcmService.Situation.GOAL_ALL_COMPLETION, seniorsMapper.findDetailByIdx(userIdx).getName(), goalsMapper.getGoalByGoalIdx(goalIdx).getTitle());
    }

    // 오늘 목표 달성률 조회
    public Double getGoalRateToday(long userIdx){
        // 오늘 날짜 가져오기
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        Double percentage = goalsStatusMapper.getGoalRateDaily(userIdx, month, day);

        if (percentage == null)
            percentage = (double) 0;
        return percentage;
    }

    // 오늘 획득한 목표 포인트 조회
    public Double getGoalPointToday(long userIdx){
        // 오늘 날짜 가져오기
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        int goalCnt = goalsStatusMapper.getGoalCntDaily(userIdx, month, day);

        return (double) (goalCnt * GOAL_POINT);
    }

    // 월별 목표 달성률 조회
    public List<Map<Integer, Double>> getGoalRateMonthly(long userIdx, int month){

        return goalsStatusMapper.getGoalRateMonthly(userIdx, month);
    }

    private void scheduleTimerForGoalStatus(GoalStatus goalStatus, Long userIdx, String userName) {
        JobDetail jobDetail1 = JobBuilder.newJob(StatusJob.class)
                .usingJobData("goalStatusIdx", goalStatus.getGoalStatusIdx()) // GoalStatusIdx를 JobData로 전달
                .withIdentity("GoalStatusUpdateJob_" + goalStatus.getGoalStatusIdx())
                .build();

        JobDetail jobDetail2 = JobBuilder.newJob(GoalAlarmJob.class)
                .usingJobData("goalStatusIdx", goalStatus.getGoalStatusIdx()) // GoalStatusIdx를 JobData로 전달
                .withIdentity("GoalStatusAlarmJob_" + goalStatus.getGoalStatusIdx())
                .build();

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("goalStatusIdx", goalStatus.getGoalStatusIdx());
        jobDataMap.put("userIdx", userIdx);
        jobDataMap.put("userName", userName);

        Trigger trigger1 = TriggerBuilder.newTrigger()
                .usingJobData(jobDataMap)
                .withIdentity("GoalStatusUpdateTrigger_" + goalStatus.getGoalStatusIdx())
                .startAt(tomorrowAt(0, 0, 0)) // 자정에 실행
                .build();

        Trigger trigger2 = TriggerBuilder.newTrigger()
                .usingJobData(jobDataMap)
                .withIdentity("GoalStatusAlarmTrigger_" + goalStatus.getGoalStatusIdx())
                .startAt(Date.from(goalStatus.getTime().toLocalTime().atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant())) // GoalStatus의 time에 따라 실행 시간 설정
                .build();

        try {
            scheduler.scheduleJob(jobDetail1, trigger1);
            scheduler.scheduleJob(jobDetail2, trigger2);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private static String buildRedisKey(String userIdx) {
        return POINT_CODE_PREFIX + userIdx;
    }
}
