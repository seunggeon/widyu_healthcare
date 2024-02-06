package com.widyu.healthcare.controller;

import com.widyu.healthcare.aop.LoginCheck;
import com.widyu.healthcare.aop.LoginCheck.UserType;
import com.widyu.healthcare.dto.SuccessResponse;
import com.widyu.healthcare.dto.goals.GoalDTO;
import com.widyu.healthcare.dto.goals.ResponseUserDTO;
import com.widyu.healthcare.service.GoalsService;
import com.widyu.healthcare.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Log4j2
@RequestMapping("/api/v1/goals")
@RequiredArgsConstructor
public class GoalsController {
    @Autowired
    private GoalsService goalsService;

    @GetMapping("/guardian/main")
    @LoginCheck(type = UserType.GUARDIAN)
    public ResponseEntity<?> getGuardianMainPage(HttpSession session){

        long userIdx = SessionUtil.getLoginGuardianId(session);
        List<ResponseUserDTO> responseUserDTOList = goalsService.getGurdianMainPage(userIdx);
        SuccessResponse response = new SuccessResponse(true, "보호자 목표 메인페이지", responseUserDTOList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/senior/main")
    @LoginCheck(type = UserType.SENIOR)
    public ResponseEntity<?> getSeniorMainPage(HttpSession session){

        long userIdx = SessionUtil.getLoginSeniorId(session);
        ResponseUserDTO responseUserDTO = goalsService.getSeniorMainPage(userIdx);
        SuccessResponse response = new SuccessResponse(true, "시니어 목표 메인페이지", responseUserDTO);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/insert")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> insertGoal(@RequestBody GoalDTO goal) {

        goalsService.insertGoal(goal);
        SuccessResponse response = new SuccessResponse(true, "목표 추가 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{goalIdx}")
    public ResponseEntity<?> deleteGoal(@PathVariable long goalIdx, HttpSession session){

        Long userIdx = Long.valueOf(SessionUtil.getLoginGuardianId(session));
        goalsService.deleteGoal(userIdx, goalIdx);
        SuccessResponse response = new SuccessResponse(true, "목표 삭제 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> editGoal(@RequestBody GoalDTO goal){
        goalsService.updateGoal(goal);
        SuccessResponse response = new SuccessResponse(true, "목표 수정 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/success/{goalStatusIdx}")
    public ResponseEntity<?> editStatusSuccess(@PathVariable long goalStatusIdx){
        goalsService.updateStatusSuccess(goalStatusIdx);
        SuccessResponse response = new SuccessResponse(true, "목표 상태 변경 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

// 부양자 전체 목표 조회 (홈) : 목표 제목, 설명, 이미지, 시간대, 전체 달성률, 달성률 응원 메세지, 시니어 달성률 리턴

// 시니어 전체 목표 조회 (홈) : 목표 제목, 설명, 이미지, 시간대, 전체 달성률, 달성률 응원 메세지

// 목표 입력 : 제목, 설명, 주기(월~일), 시간대(배열로), 사진찍기 유무, 약 갯수

// 목표 수정 : 제목, 설명, 주기(월~일), 시간대(배열로), 사진찍기 유무, 약 갯수 -> 시간대 별 약 갯수 테이블 따로 빼는 게 낫겠는데?

// 목표 조회 : 제목, 설명, 주기(월~일), 시간대(배열로), 사진찍기 유무, 약 갯수 -> 시간대 별 약 갯수 테이블 따로 빼는 게 낫겠는데?

// 목표 삭제 : 제목, 설명, 주기(월~일), 시간대(배열로), 사진찍기 유무, 약 갯수 -> 시간대 별 약 갯수 테이블 따로 빼는 게 낫겠는데?

// 사진 촬영 후 업로드 : s3로

// 사진 링크 수정 : s3로

// 사진 링크 삭제 :

// 타이머 어떻게 할지.