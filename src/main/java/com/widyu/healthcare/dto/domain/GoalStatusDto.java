package com.widyu.healthcare.dto.domain;

import jakarta.annotation.Nullable;
import lombok.*;

import java.sql.Time;

@Data
public class GoalStatusDto {

    private long goalStatusIdx;
    @Nullable
    private long goalIdx;
    @NonNull
    private Time time;
    private byte status;
    private Long pillNum;
    private String imgUrl;
    private long point;

    public GoalStatusDto(long goalIdx, @NonNull Time time, byte status, Long pillNum, String imgUrl, long point) {
        this.goalIdx = goalIdx;
        this.time = time;
        this.status = status;
        this.pillNum = pillNum;
        this.imgUrl = imgUrl;
        this.point = point;
    }
}
