package com.widyu.healthcare.dto.domain;

import lombok.*;

import java.sql.Time;

@Data
public class GoalStatusDto {

    private long goalStatusIdx;
    private long goalIdx;
    @NonNull
    private Time time;
    private byte status;
    private Long pillNum;
    private String imgUrl;
    private long point;

    public GoalStatusDto(@NonNull Time time, Long pillNum, long point) {
        this.time = time;
        this.status = status;
        this.pillNum = pillNum;
        this.imgUrl = imgUrl;
        this.point = point;
    }
}
