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
    private Timestamp startDate;
    private Timestamp endDate;

    public GoalDTO(@NonNull Long userIdx, @NonNull String title, String description, @NonNull Type type, String day, Timestamp startDate, Timestamp endDate) {
        this.userIdx = userIdx;
        this.title = title;
        this.description = description;
        this.type = type;
        this.day = day;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}

enum Type {
    MEDICATION,
    WALKING,
    GENERAL
}