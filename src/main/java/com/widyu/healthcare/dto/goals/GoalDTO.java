package com.widyu.healthcare.dto.goals;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GoalDTO {

    private long goalIdx;
    @NonNull
    private long userIdx;
    @NonNull
    private String title;
    private String description;
    @NonNull
    private Type type;
    private String day;

    public GoalDTO(@NonNull Long userIdx, @NonNull String title, String description, @NonNull Type type, String day) {
        this.userIdx = userIdx;
        this.title = title;
        this.description = description;
        this.type = type;
        this.day = day;
    }
}

enum Type {
    MEDICATION,
    WALKING,
    GENERAL
}