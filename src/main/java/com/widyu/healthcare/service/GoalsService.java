package com.widyu.healthcare.service;

import com.widyu.healthcare.dto.request.GoalSetRequestDto;
import com.widyu.healthcare.dto.response.GuardianGoalResponseDto;
import com.widyu.healthcare.dto.response.SeniorGoalResponseDto;
import com.widyu.healthcare.dto.response.MainGoalResponseDto;
import com.widyu.healthcare.jobs.StatusJob;
import com.widyu.healthcare.mapper.GoalsStatusMapper;
import com.widyu.healthcare.dto.domain.GoalStatusDto;
import com.widyu.healthcare.mapper.GoalsMapper;
import com.widyu.healthcare.mapper.GuardiansMapper;
import com.widyu.healthcare.mapper.SeniorsMapper;
import com.widyu.healthcare.utils.GoalUtil;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


@Log4j2
@Service
public class GoalsService {

    private final GoalsMapper goalsMapper;
    private final GuardiansMapper guardiansMapper;
    private final GoalsStatusMapper goalsStatusMapper;
    private final RedisService redisService;
    private final FcmService fcmService;
    private final Scheduler scheduler;
    private static final String POINT_CODE_PREFIX = "point_code:";

    @Autowired
    public GoalsService(GoalsMapper goalsMapper, GuardiansMapper guardiansMapper, SeniorsMapper seniorsMapper, GoalsStatusMapper goalsStatusMapper, GuardiansService guardiansService, SeniorsService seniorsService, RedisService redisService, FcmService fcmService, Scheduler scheduler) {

        this.goalsMapper = goalsMapper;
        this.guardiansMapper = guardiansMapper;
        this.goalsStatusMapper = goalsStatusMapper;
        this.redisService = redisService;
        this.fcmService = fcmService;
        this.scheduler = scheduler;
    }

    public MainGoalResponseDto getGuardianMainPage(long userIdx){

        // 본인 목표 정보
        GuardianGoalResponseDto myGoal = GoalUtil.calculatePercentageAndReturn(goalsMapper.findByGuardianIdx(userIdx));

        // 시니어(가족) 정보
        List<Long> seniorsIdxOnFamily = guardiansMapper.findSeniorsIdxByIdx(userIdx);
        List<SeniorGoalResponseDto> seniorsGoalList;
        seniorsGoalList = seniorsIdxOnFamily.stream()
                .map(seniorIdx -> goalsMapper.findBySeniorIdx(seniorIdx))
                .peek(dto -> dto.percentageFromGoals())
                .collect(Collectors.toList());

        MainGoalResponseDto mainGoal = MainGoalResponseDto.builder()
                .guardianGoalResponseDto(myGoal)
                .seniorGoalResponseDtoList(seniorsGoalList)
                .build();

        return mainGoal;
    }

    public SeniorGoalResponseDto getSeniorMainPage(long userIdx){
        // 본인 목표 정보
        SeniorGoalResponseDto myGoal = GoalUtil.calculatePercentageAndReturn(goalsMapper.findBySeniorIdx(userIdx));

        return myGoal;
    }

    // 보호자 메인 목표 화면
//    public List<ResponseUserDTO> getGuardianMainPage(long userIdx){
//
//        List<ResponseUserDTO> responseUserDTOList = new ArrayList<>();
//
//        // gurdian Info
//        responseUserDTOList.add(getResponseUserDtoByUserIdx(userIdx));
//
//        // senior Info
//        List<SeniorDetailResponseDto> seniorsList = guardiansService.getSeniorsOrNull(userIdx);
//        for (SeniorDetailResponseDto seniorDetailResponseDto : seniorsList) {
//            ResponseUserDTO seniorResponseUserDTO = new ResponseUserDTO();
//
//            seniorResponseUserDTO.setName(seniorDetailResponseDto.getName());
//            seniorResponseUserDTO.setUserType(SENIOR);
//            // *userTable에서 가져와야할 정보 추후
//
//            seniorResponseUserDTO.setGoals(getGoalsByIdx(seniorDetailResponseDto.getUserIdx()));
//            responseUserDTOList.add(seniorResponseUserDTO);
//        }
//
//
//        return responseUserDTOList;
//    }

//    // 시니어 메인 목표 화면
//    public ResponseUserDTO getSeniorMainPage(long userIdx){
//        return getResponseUserDtoByUserIdx(userIdx);
//    }


    //
//    private ResponseUserDTO getResponseUserDtoByUserIdx(long userIdx){
//
//        ResponseUserDTO responseUserDTO = new ResponseUserDTO();
//        // *userTable에서 가져와야할 정보 추후
//        List<GoalSetDTO> goalSetDTOList= getGoalsByIdx(userIdx);
//        responseUserDTO.setGoals(getGoalsByIdx(userIdx));
//        return responseUserDTO;
//    }

//    // 목표 전체 조회
//    public List<GoalSetDTO> getGoalsByIdx(long userIdx){
//
//        List<GoalSetDTO> GoalSetList = new ArrayList<GoalSetDTO>();
//        List<GoalDTO> goals = goalsMapper.getGoalsByIdx(userIdx);
//
//        for (GoalDTO goal : goals) {
//
//            Long goalIdx = goal.getGoalIdx();
//            List<GoalStatusDTO> goalStatuses = goalsStatusMapper.getGoalStatusesByGoalIdx(goalIdx);
//            GoalSetDTO goalSetDTO = new GoalSetDTO(goal, goalStatuses);
//            GoalSetList.add(goalSetDTO);
//        }
//
//        return GoalSetList;
//    }

    // 특정 단일 목표 조회
//    public GoalDTO getGoalByGoalIdx(long userIdx, long goalIdx){
//
//        return goalsMapper.getGoalByGoalIdx(userIdx, goalIdx);
//    }

    // 목표 생성
    public GoalSetRequestDto insertGoal(GoalSetRequestDto goalSet){
        goalsMapper.insertGoal(goalSet.getGoalDto());
        Long goalIdx = goalsMapper.getGoalIdx(goalSet.getGoalDto());

        for (GoalStatusDto goalStatus : goalSet.getGoalStatusDtoList()) {
            goalStatus.setGoalIdx(goalIdx);
            goalStatus.setStatus((byte) 0);
            goalsStatusMapper.insertGoalStatus(goalStatus);
            Long goalStatusIdx = goalsStatusMapper.getGoalStatusIdx(goalStatus);
            goalStatus.setGoalStatusIdx(goalStatusIdx);

            //timer
            scheduleTimerForGoalStatus(goalStatus);
        }
        return goalSet;
    }

    // 목표 수정
    public void updateGoal(GoalSetRequestDto goalSetDto){

        goalsMapper.updateGoal(goalSetDto.getGoalDto());
        for (GoalStatusDto goalStatus : goalSetDto.getGoalStatusDtoList()) {
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
        // 포인트 추가 (*10 포인트 추가로 설정함)
        goalsStatusMapper.updateTotalPoint(userIdx, 10L);
        redisService.incrementPoint(buildRedisKey(userIdx.toString()));
        log.info("[RP] redis point: {}", redisService.getPoint(userIdx.toString()));

        //
        //fcmService.sendMessage();
    }

    private void scheduleTimerForGoalStatus(GoalStatusDto goalStatus) {
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
        return POINT_CODE_PREFIX + userIdx;
    }
}
