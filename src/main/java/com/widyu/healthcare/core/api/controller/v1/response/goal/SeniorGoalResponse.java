package com.widyu.healthcare.core.api.controller.v1.response.goal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.widyu.healthcare.core.api.controller.v1.response.CommonUserResponse;
import com.widyu.healthcare.core.api.controller.v1.response.guardian.GuardianInfoResponse;
import com.widyu.healthcare.core.domain.domain.v1.GoalCalculatable;
import com.widyu.healthcare.core.domain.domain.v1.Goal;
import com.widyu.healthcare.core.domain.domain.v1.GoalStatus;
import com.widyu.healthcare.core.domain.domain.v1.User;
import com.widyu.healthcare.support.utils.GoalUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
/**
 * 시니어 메인 화면
 * 보호자 메인 화면 attribute
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class SeniorGoalResponse extends CommonUserResponse implements GoalCalculatable {

    @JsonIgnore
    private double achieveGoal;
    @JsonIgnore
    private double totalGoal;
    @JsonProperty("percentage")
    private double percentageOfGoal;
    // senior property 에만 존재. oldIndex, point
    @JsonProperty("oldIndex")
    private long sequence;
    @JsonProperty("point")
    private int totalPoint;
    private List<Goal> goals;

    @Builder
    public SeniorGoalResponse(List<Goal> goals, long sequence, double percentageOfGoal){
        this.goals = goals;
        this.sequence = sequence;
        this.percentageOfGoal = percentageOfGoal;
    }


    @Override
    public void percentageFromGoals() {
        this.percentageOfGoal = GoalUtil.calculatePercentageOfGoal(this.achieveGoal, this.totalGoal);
    }
}
