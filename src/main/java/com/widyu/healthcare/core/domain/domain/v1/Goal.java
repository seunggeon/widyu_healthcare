package com.widyu.healthcare.core.domain.domain.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class Goal {
    private long goalIdx;
    @NonNull
    private long userIdx;
    @NonNull
    private String title;
    private String description;
    private GoalType type;
    private String day;
    @JsonProperty("goalStatusList")
    private List<GoalStatus> goalStatusList;

    @Builder
    public Goal(@NonNull long userIdx, @NonNull String title, String description, GoalType type, String day) {
        this.userIdx = userIdx;
        this.title = title;
        this.description = description;
        this.type = type;
        this.day = day;
    }
    public List<GoalStatus> toGoalStatusList() {
        List<GoalStatus> GoalStatusList = this.goalStatusList;
        return GoalStatusList;
    }
}

