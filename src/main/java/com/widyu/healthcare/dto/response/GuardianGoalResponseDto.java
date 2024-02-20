package com.widyu.healthcare.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.widyu.healthcare.dto.GoalCalculatable;
import com.widyu.healthcare.utils.GoalUtil;
import lombok.Getter;
import java.util.List;

import com.widyu.healthcare.dto.goals.GoalDto;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class GuardianGoalResponseDto extends UsersResponseDto implements GoalCalculatable {
    @JsonIgnore
    private double achieveGoal;
    @JsonIgnore
    private double totalGoal;
    @JsonProperty("percentage")
    private double percentageOfGoal;
    private List<GoalDto> goals;
    @Override
    public void percentageFromGoals() {
        this.percentageOfGoal = GoalUtil.calculatePercentageOfGoal(this.achieveGoal, this.totalGoal);
    }

//    public GuardianGoalResponseDto(totalPoint, achieveGoal, totalGoal) {
//        this.totalPoint = totalPoint;
//        this.percentageOfGoal = this.percentageFromGoals(achieveGoal, totalGoal);
//    }
}
