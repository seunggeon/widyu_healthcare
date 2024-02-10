package com.widyu.healthcare.service;

import com.widyu.healthcare.dto.goals.GoalDTO;
import com.widyu.healthcare.dto.goals.ResponseUserDTO;
import com.widyu.healthcare.dto.users.UsersDTO;
import com.widyu.healthcare.mapper.GoalsStatusMapper;
import com.widyu.healthcare.dto.goals.GoalStatus;
import com.widyu.healthcare.mapper.GoalsMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Log4j2
@Service
public class GoalsService {

    private final GoalsMapper goalsMapper;
    private final GoalsStatusMapper goalsStatusMapper;
    private final GuardiansService guardiansService;
    private final RedisService redisService;
    private static final String VERIFICATION_CODE_PREFIX = "point_code:";

    @Autowired
    public GoalsService(GoalsMapper goalsMapper, GoalsStatusMapper goalsStatusMapper, GuardiansService guardiansService, RedisService redisService) {

        this.goalsMapper = goalsMapper;
        this.goalsStatusMapper = goalsStatusMapper;
        this.guardiansService = guardiansService;
        this.redisService = redisService;
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
            seniorResponseUserDTO.setGoals(getGoalsById(usersDTO.getUserIdx()));
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
        responseUserDTO.setGoals(getGoalsById(userIdx));
        return responseUserDTO;
    }

    // 목표 전체 조회
    public List<GoalDTO> getGoalsById(long id){

        return goalsMapper.getGoalsById(id);
    }

    // 특정 단일 목표 조회
    public GoalDTO getGoalByGoalId(long userIdx, long goalIdx){

        return goalsMapper.getGoalByGoalId(userIdx, goalIdx);
    }

    // 목표 생성
    public GoalDTO insertGoal(GoalDTO goal){
        goalsMapper.insertGoal(goal);
        Long goalIdx = goalsMapper.getGoalIdx(goal);

        for (GoalStatus goalStatus : goal.getGoalStatusList()) {
            goalStatus.setGoalIdx(goalIdx);
            goalsStatusMapper.insertGoalStatus(goalStatus);
            Long goalStatusIdx = goalsStatusMapper.getGoalStatusIdx(goalStatus);
            goalStatus.setGoalStatusIdx(goalStatusIdx);
        }
        return goal;
    }

    // 목표 수정
    public void updateGoal(GoalDTO goal){

        goalsMapper.updateGoal(goal);
        for (GoalStatus goalStatus : goal.getGoalStatusList()) {
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
        goalsStatusMapper.updateStatusSuccess(goalStatusIdx);
        // 포인트 추가 (*1포인트 추가로 설정함)
        goalsStatusMapper.updateTotalPoint(userIdx, 1L);
        redisService.incrementPoint(buildRedisKey(userIdx.toString()));
        //log.info("redis-point: {}", redisService.getPoint(buildRedisKey(userIdx.toString()));
    }

    private static String buildRedisKey(String userIdx) {
        return VERIFICATION_CODE_PREFIX + userIdx;
    }
}
