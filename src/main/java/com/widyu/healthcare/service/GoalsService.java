package com.widyu.healthcare.service;

import com.widyu.healthcare.dto.goals.GoalDTO;
import com.widyu.healthcare.dto.goals.GoalSetDTO;
import com.widyu.healthcare.dto.goals.ResponseUserDTO;
import com.widyu.healthcare.dto.users.UsersDTO;
import com.widyu.healthcare.jobs.StatusJob;
import com.widyu.healthcare.mapper.GoalsStatusMapper;
import com.widyu.healthcare.dto.goals.GoalStatusDTO;
import com.widyu.healthcare.mapper.GoalsMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.jdbc.Null;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


@Log4j2
@Service
public class GoalsService {

    private final GoalsMapper goalsMapper;
    private final GoalsStatusMapper goalsStatusMapper;
    private final GuardiansService guardiansService;
    private final RedisService redisService;
    private final Scheduler scheduler;
    private static final String VERIFICATION_CODE_PREFIX = "point_code:";

    @Autowired
    public GoalsService(GoalsMapper goalsMapper, GoalsStatusMapper goalsStatusMapper, GuardiansService guardiansService, RedisService redisService, Scheduler scheduler) {

        this.goalsMapper = goalsMapper;
        this.goalsStatusMapper = goalsStatusMapper;
        this.guardiansService = guardiansService;
        this.redisService = redisService;
        this.scheduler = scheduler;
    }

    // 보호자 메인 목표 화면
    public List<ResponseUserDTO> getGurdianMainPage(long userIdx){

        List<ResponseUserDTO> responseUserDTOList = new ArrayList<>();

        // gurdian Info
        responseUserDTOList.add(getResponseUserDtoByUserIdx(userIdx));

        // senior Info
        List<UsersDTO> seniorsList = guardiansService.getSeniorsOrNull(userIdx);
        for (UsersDTO usersDTO : seniorsList) {
            ResponseUserDTO seniorResponseUserDTO = new ResponseUserDTO();

            seniorResponseUserDTO.setName(usersDTO.getName());
            seniorResponseUserDTO.setUserType(usersDTO.getType());
            // *userTable에서 가져와야할 정보 추후

            seniorResponseUserDTO.setGoals(getGoalsByIdx(usersDTO.getUserIdx()));
            responseUserDTOList.add(seniorResponseUserDTO);
        }

        return responseUserDTOList;
    }

    // 시니어 메인 목표 화면
    public ResponseUserDTO getSeniorMainPage(long userIdx){
        return getResponseUserDtoByUserIdx(userIdx);
    }


    //
    private ResponseUserDTO getResponseUserDtoByUserIdx(long userIdx){

        ResponseUserDTO responseUserDTO = new ResponseUserDTO();
        // *userTable에서 가져와야할 정보 추후
        List<GoalSetDTO> goalSetDTOList= getGoalsByIdx(userIdx);
        responseUserDTO.setGoals(getGoalsByIdx(userIdx));
        return responseUserDTO;
    }

    // 목표 전체 조회
    public List<GoalSetDTO> getGoalsByIdx(long userIdx){

        List<GoalSetDTO> GoalSetList = new ArrayList<GoalSetDTO>();
        List<GoalDTO> goals = goalsMapper.getGoalsByIdx(userIdx);

        for (GoalDTO goal : goals) {

            Long goalIdx = goal.getGoalIdx();
            List<GoalStatusDTO> goalStatuses = goalsStatusMapper.getGoalStatusesByGoalIdx(goalIdx);
            GoalSetDTO goalSetDTO = new GoalSetDTO(goal, goalStatuses);
            GoalSetList.add(goalSetDTO);
        }

        return GoalSetList;
    }

    // 특정 단일 목표 조회
    public GoalDTO getGoalByGoalId(long userIdx, long goalIdx){

        return goalsMapper.getGoalByGoalIdx(userIdx, goalIdx);
    }

    // 목표 생성
    public GoalSetDTO insertGoal(GoalSetDTO goalSet){
        goalsMapper.insertGoal(goalSet.getGoalDTO());
        Long goalIdx = goalsMapper.getGoalIdx(goalSet.getGoalDTO());

        for (GoalStatusDTO goalStatus : goalSet.getGoalStatusDTOList()) {
            goalStatus.setGoalIdx(goalIdx);
            goalsStatusMapper.insertGoalStatus(goalStatus);
            Long goalStatusIdx = goalsStatusMapper.getGoalStatusIdx(goalStatus);
            goalStatus.setGoalStatusIdx(goalStatusIdx);

            //timer
            scheduleTimerForGoalStatus(goalStatus);
        }
        return goalSet;
    }

    // 목표 수정
    public void updateGoal(GoalSetDTO goalSetDTO){

        goalsMapper.updateGoal(goalSetDTO.getGoalDTO());
        for (GoalStatusDTO goalStatus : goalSetDTO.getGoalStatusDTOList()) {
            goalsStatusMapper.updateGoalStatus(goalStatus);
        }
    }

    // 목표 삭제
    public void deleteGoal(long userIdx, long goalIdx){
        goalsStatusMapper.deleteGoalStatus(goalIdx);
        goalsMapper.deleteGoal(userIdx, goalIdx);
    }

    // 목표 상태 수정 (성공)
    public void updateStatusSuccess(Long userIdx, long goalStatusIdx){
        // 상태 성공으로 변경
        goalsStatusMapper.updateStatus(goalStatusIdx, 1L);
        // 포인트 추가 (*1포인트 추가로 설정함)
        goalsStatusMapper.updateTotalPoint(userIdx, 1L);
        redisService.incrementPoint(buildRedisKey(userIdx.toString()));
        //log.info("redis-point: {}", redisService.getPoint(buildRedisKey(userIdx.toString()));
    }

    private void scheduleTimerForGoalStatus(GoalStatusDTO goalStatus) {
        JobDetail jobDetail = JobBuilder.newJob(StatusJob.class)
                .usingJobData("goalStatusIdx", goalStatus.getGoalStatusIdx()) // GoalStatusIdx를 JobData로 전달
                .withIdentity("GoalStatusUpdateJob_" + goalStatus.getGoalStatusIdx())
                .build();

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("goalStatusIdx", goalStatus.getGoalStatusIdx());

        Trigger trigger = TriggerBuilder.newTrigger()
                .usingJobData(jobDataMap)
                .withIdentity("GoalStatusUpdateTrigger_" + goalStatus.getGoalStatusIdx())
                .startAt(Date.from(goalStatus.getTime().toLocalTime().atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant())) // GoalStatus의 time에 따라 실행 시간 설정
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private static String buildRedisKey(String userIdx) {
        return VERIFICATION_CODE_PREFIX + userIdx;
    }
}
