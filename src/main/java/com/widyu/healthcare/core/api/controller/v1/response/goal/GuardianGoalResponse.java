package com.widyu.healthcare.core.api.controller.v1.response.goal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.widyu.healthcare.core.api.controller.v1.response.CommonUserResponse;
import com.widyu.healthcare.core.domain.domain.v1.GoalCalculatable;
import com.widyu.healthcare.support.utils.GoalUtil;
import lombok.Getter;
import java.util.List;

import com.widyu.healthcare.core.domain.domain.v1.Goal;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class GuardianGoalResponse extends CommonUserResponse implements GoalCalculatable {
    @JsonIgnore
    private double achieveGoal;
    @JsonIgnore
    private double totalGoal;
    @JsonProperty("percentage")
    private double percentageOfGoal;
    private List<Goal> goalsAndStatus;
    @Override
    public void percentageFromGoals() {
        this.percentageOfGoal = GoalUtil.calculatePercentageOfGoal(this.achieveGoal, this.totalGoal);
    }
}
