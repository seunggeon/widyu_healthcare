package com.widyu.healthcare.utils;

import com.widyu.healthcare.dto.GoalCalculatable;
import com.widyu.healthcare.dto.response.GuardianGoalResponseDto;

public class GoalUtil {
    public static double calculatePercentageOfGoal(double achieveGoal, double totalGoal) {
        if (totalGoal > 0) {
            return (achieveGoal / totalGoal) * 100;
        } else {
            return 0.0;
        }
    }
    public static <T extends GoalCalculatable> T calculatePercentageAndReturn(T goalDto) {
        goalDto.percentageFromGoals();
        return goalDto;
    }
}
