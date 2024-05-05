package com.widyu.healthcare.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.widyu.healthcare.dto.GoalCalculatable;
import com.widyu.healthcare.dto.domain.GoalDto;
import com.widyu.healthcare.utils.GoalUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.util.List;
/**
 * 시니어 메인 화면
 * 보호자 메인 화면 attribute
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class SeniorGoalResponseDto extends UsersResponseDto implements GoalCalculatable {
    @JsonIgnore
    private double achieveGoal;
    @JsonIgnore
    private double totalGoal;
    @JsonProperty("percentage")
    private double percentageOfGoal;
    // senior property 추가. oldIndex, point
    @JsonProperty("oldIndex")
    private long sequence;
    @JsonProperty("point")
    private int totalPoint;
    private List<GoalDto> goalsAndStatus;
    @Override
    public void percentageFromGoals() {
        this.percentageOfGoal = GoalUtil.calculatePercentageOfGoal(this.achieveGoal, this.totalGoal);
    }
}
