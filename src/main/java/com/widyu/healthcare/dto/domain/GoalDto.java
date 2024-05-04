package com.widyu.healthcare.dto.domain;

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


    public GoalDto(@NonNull Long userIdx, @NonNull String title, String description, GoalType type, String day) {
        this.userIdx = userIdx;
        this.title = title;
        this.description = description;
        this.type = type;
        this.day = day;
    }
}

