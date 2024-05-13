package com.widyu.healthcare.support.utils;

import com.widyu.healthcare.core.domain.domain.v1.GoalCalculatable;

public class GoalUtil {
    public static double calculatePercentageOfGoal(double achieveGoal, double totalGoal) {
        if (totalGoal > 0) {
            return (achieveGoal / totalGoal) * 100;
        } else {
            return 0.0;
        }
    }
    public static <T extends GoalCalculatable> T calculatePercentageAndReturn(T goal) {
        goal.percentageFromGoals();
        return goal;
    }
}
