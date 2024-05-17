package com.widyu.healthcare.core.domain.domain.v1;

import jakarta.annotation.Nullable;
import lombok.*;

import java.sql.Time;

@Getter
public class GoalStatus {

    private long goalStatusIdx;
    @Nullable
    private long goalIdx;
    @NonNull
    private Time time;
    @NonNull
    private byte status;
    private Long pillNum;
    private String imgUrl;
    private long point;
    @Builder
    public GoalStatus(long goalIdx, @NonNull Time time, Long pillNum, String imgUrl, long point) {
        this.goalIdx = goalIdx;
        this.time = time;
        this.status = (byte)0;
        this.pillNum = pillNum;
        this.imgUrl = imgUrl;
        this.point = point;
    }

    public void setGoalIdx(long goalIdx){
        this.goalIdx = goalIdx;
    }
}
