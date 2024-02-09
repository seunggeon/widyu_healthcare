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

    /**
     * 보호자 목표조 회
     * @param session
     * @return
     */
    @GetMapping("/guardian/main")
    @LoginCheck(type = UserType.GUARDIAN)
    public ResponseEntity<?> getGuardianMainPage(HttpSession session){

        long userIdx = SessionUtil.getLoginGuardianId(session);
        List<ResponseUserDTO> responseUserDTOList = goalsService.getGurdianMainPage(userIdx);
        SuccessResponse response = new SuccessResponse(true, "보호자 목표 메인페이지", responseUserDTOList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 시니어 목표 조회
     * @param session
     * @return
     */
    @GetMapping("/senior/main")
    @LoginCheck(type = UserType.SENIOR)
    public ResponseEntity<?> getSeniorMainPage(HttpSession session){

        long userIdx = SessionUtil.getLoginSeniorId(session);
        ResponseUserDTO responseUserDTO = goalsService.getSeniorMainPage(userIdx);
        SuccessResponse response = new SuccessResponse(true, "시니어 목표 메인페이지", responseUserDTO);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 목표 생성
     * @param goal
     * @return
     */
    @PostMapping("/insert")
    public ResponseEntity<?> insertGoal(@RequestBody GoalDTO goal) {

        goalsService.insertGoal(goal);
        SuccessResponse response = new SuccessResponse(true, "목표 추가 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 목표 삭제
     * @param goalIdx
     * @param session
     * @return
     */
    @DeleteMapping("/delete/{goalIdx}")
    public ResponseEntity<?> deleteGoal(@PathVariable long goalIdx, HttpSession session){

        Long userIdx = Long.valueOf(SessionUtil.getLoginGuardianId(session));
        goalsService.deleteGoal(userIdx, goalIdx);
        SuccessResponse response = new SuccessResponse(true, "목표 삭제 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 목표 수정
     * @param goal
     * @return
     */
    @PatchMapping("/edit")
    public ResponseEntity<?> editGoal(@RequestBody GoalDTO goal){
        goalsService.updateGoal(goal);
        SuccessResponse response = new SuccessResponse(true, "목표 수정 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 목표 상태 변경 (성공)
     * @param goalStatusIdx
     * @return
     */
    @PatchMapping("/success/{goalStatusIdx}")
    public ResponseEntity<?> editStatusSuccess(@PathVariable long goalStatusIdx, HttpSession session){


        Long userIdx = Long.valueOf(SessionUtil.getLoginGuardianId(session));
        goalsService.updateStatusSuccess(userIdx, goalStatusIdx);
        SuccessResponse response = new SuccessResponse(true, "목표 상태 변경 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}