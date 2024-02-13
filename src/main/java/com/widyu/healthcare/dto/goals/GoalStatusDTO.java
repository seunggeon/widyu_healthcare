package com.widyu.healthcare.dto.goals;

import com.google.firebase.database.annotations.NotNull;
import lombok.*;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class GoalStatusDTO {

    private long goalStatusIdx;
    private long goalIdx;
    @NonNull
    private Time time;
    private byte status;
    private Long pillNum;
    private String imgUrl;
    private long point;

    public GoalStatusDTO(@NonNull Time time, Long pillNum, long point) {
        this.time = time;
        this.status = status;
        this.pillNum = pillNum;
        this.imgUrl = imgUrl;
        this.point = point;
    }
}
