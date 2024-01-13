package com.widyu.healthcare.controller;

import com.widyu.healthcare.aop.LoginCheck;
import com.widyu.healthcare.aop.LoginCheck.UserType;
import com.widyu.healthcare.service.GoalsService;
import com.widyu.healthcare.dto.goals.Goal;
import com.widyu.healthcare.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
public class GoalsController {
    @Autowired
    private GoalsService goalsService;

    @GetMapping("/all")
    @LoginCheck(type = UserType.GUARDIAN)
    public List<Goal> getAllGoals(HttpSession session){

        Integer userIdx = SessionUtil.getLoginGuardianId(session);
        return goalsService.getGoalsById(userIdx);
    }

    @PostMapping("/insert")
    @ResponseStatus(HttpStatus.OK)
    public void insertGoal(@RequestBody Goal goal) {

        goalsService.insertGoal(goal);
    }

    @DeleteMapping("/delete/{goalIdx}")
    public void deleteGoal(@PathVariable long goalIdx, HttpSession session){

        Integer userIdx = SessionUtil.getLoginGuardianId(session);
        goalsService.deleteGoal(userIdx, goalIdx);
    }

    @PatchMapping("/edit/{goalIdx}")
    public void editGoal(@PathVariable long goalIdx, @RequestBody Goal goal){
        goalsService.updateGoal(goalIdx, goal);
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