package com.widyu.healthcare.core.domain.domain.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
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
    @Size(max = 500, message = "description should be up to 500 characters")
    private String description;
    @Size(max = 10, message = "type should be up to 10 characters")
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

