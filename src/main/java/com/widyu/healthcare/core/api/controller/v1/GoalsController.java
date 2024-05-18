package com.widyu.healthcare.core.api.controller.v1;

import com.widyu.healthcare.core.api.controller.v1.request.goal.AppendGoalRequest;
import com.widyu.healthcare.core.api.controller.v1.request.goal.UpdateGoalRequest;
import com.widyu.healthcare.core.api.controller.v1.response.goal.MainGoalResponse;
import com.widyu.healthcare.core.api.controller.v1.response.goal.SeniorGoalResponse;
import com.widyu.healthcare.core.api.controller.v1.response.SuccessResponse;

import com.widyu.healthcare.core.domain.domain.v1.Goal;
import com.widyu.healthcare.core.domain.service.v1.GoalsService;
import com.widyu.healthcare.support.utils.SessionUtil;
import com.widyu.healthcare.core.api.middleware.LoginCheck;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;


@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/goals")
public class GoalsController {
    private final GoalsService goalsService;
    /**
     * 보호자 목표 조회
     * @param apiUser
     * @return targetUserGoalsAndSeniorGoals
     */
    @GetMapping("/guardian/main")
    @LoginCheck(type = LoginCheck.UserType.GUARDIAN)
    public ResponseEntity<?> getGuardianMainPage(HttpSession apiUser){

        long targetIdx = SessionUtil.getLoginGuardianIdx(apiUser);
        MainGoalResponse targetUserGoalsAndSeniorGoals = goalsService.getTargetUserGoalsAndSeniorGoals(targetIdx);
        SuccessResponse response = new SuccessResponse(true, "보호자 목표 메인 페이지 조회 성공", targetUserGoalsAndSeniorGoals);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 시니어 목표 조회
     * @param apiUser
     * @return targetUserGoals
     */
    @GetMapping("/senior/main")
    @LoginCheck(type = LoginCheck.UserType.SENIOR)
    public ResponseEntity<?> getSeniorMainPage(HttpSession apiUser){

        long targetIdx = SessionUtil.getLoginSeniorIdx(apiUser);
        SeniorGoalResponse targetUserGoals = goalsService.getTargetSeniorGoals(targetIdx);
        SuccessResponse response = new SuccessResponse(true, "시니어 목표 메인 페이지 조회 성공", targetUserGoals);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 목표 생성
     * @param goal
     * @return
     */
    @PostMapping("/insert")
    public ResponseEntity<?> insertGoal(@RequestBody Goal goal) {

        Goal resultGoal = goalsService.insertGoal(goal, goal.getGoalStatusList());
        SuccessResponse response = new SuccessResponse(true, "목표 추가 성공", resultGoal);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 목표 삭제
     * @param goalIdx
     * @return
     */
    @DeleteMapping("/delete/{goalIdx}")
    @LoginCheck(type = LoginCheck.UserType.COMMON)
    public ResponseEntity<?> deleteGoal(@PathVariable long goalIdx){

        goalsService.deleteGoal(goalIdx);
        SuccessResponse response = new SuccessResponse(true, "목표 삭제 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 목표 수정
     * @param
     * @return
     */
    @PatchMapping("/edit")
    @LoginCheck(type = LoginCheck.UserType.COMMON)
    public ResponseEntity<?> editGoal(@RequestBody Goal goal){
        goalsService.updateGoal(goal, goal.getGoalStatusList());
        SuccessResponse response = new SuccessResponse(true, "목표 수정 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 목표 상태 변경 (성공)
     * @param goalStatusIdx
     * @return
     */
    @PatchMapping("/success/{goalStatusIdx}")
    @LoginCheck(type = LoginCheck.UserType.COMMON)
    public ResponseEntity<?> editStatusSuccess(@PathVariable long goalStatusIdx, HttpSession apiUser) throws IOException {

        long userIdx = SessionUtil.getLoginGuardianIdx(apiUser);
        goalsService.updateStatusSuccess(userIdx, goalStatusIdx);
        SuccessResponse response = new SuccessResponse(true, "목표 상태 변경 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}