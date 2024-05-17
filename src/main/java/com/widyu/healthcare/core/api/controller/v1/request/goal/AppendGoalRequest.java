package com.widyu.healthcare.core.api.controller.v1.request.goal;

import com.widyu.healthcare.core.domain.domain.v1.Goal;
import com.widyu.healthcare.core.domain.domain.v1.GoalStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
public class AppendGoalRequest {

    @NonNull
    private Goal goal;
    @Builder
    public AppendGoalRequest(Goal goal, List<GoalStatus> goalStatusList) {
        this.goal = goal;
    }
    public Goal toGoal() {
        Goal goal = this.goal;
        return goal;
    }
}