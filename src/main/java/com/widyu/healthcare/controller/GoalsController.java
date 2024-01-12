package com.widyu.healthcare.controller;

import com.widyu.healthcare.service.GoalsService;
import com.widyu.healthcare.dto.goals.Goal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
public class GoalsController {
    @Autowired
    private GoalsService goalsService;
    @GetMapping("/allGoals/{userIdx}")
    public List<Goal> getAllGoals(@PathVariable long userIdx){
        return goalsService.getGoalsById(userIdx);
    }

    @PostMapping("/insert")
    public void insertGoal(@RequestBody Goal goal) {
        goalsService.insertGoal(goal);
    }

    @DeleteMapping("/delete/{userIdx}/{goalIdx}")
    public void deleteGoal(@PathVariable long userIdx, long goalIdx){
        goalsService.deleteGoal(userIdx, goalIdx);
    }

    @PutMapping("/edit/{goalIdx}")
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