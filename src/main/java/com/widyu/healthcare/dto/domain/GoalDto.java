package com.widyu.healthcare.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.widyu.healthcare.dto.GoalType;
import com.widyu.healthcare.dto.domain.GoalStatusDto;
import lombok.*;
import java.util.List;

@Data
public class GoalDto {
    private long goalIdx;
    @NonNull
    private long userIdx;
    @NonNull
    private String title;
    private String description;
    private GoalType type;
    private String day;
    @JsonProperty("goalStatusList")
    private List<GoalStatusDto> goalStatusDtoList;

    public GoalDto(@NonNull Long userIdx, @NonNull String title, String description, GoalType type, String day, List<GoalStatusDto> goalStatusDtoList) {
        this.userIdx = userIdx;
        this.title = title;
        this.description = description;
        this.type = type;
        this.day = day;
        this.goalStatusDtoList = goalStatusDtoList;
    }
}

