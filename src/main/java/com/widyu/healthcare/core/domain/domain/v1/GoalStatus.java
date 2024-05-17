package com.widyu.healthcare.core.domain.domain.v1;

import jakarta.annotation.Nullable;
import lombok.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

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
    public GoalStatus(long goalStatusIdx, long goalIdx, @NonNull Time time, Long pillNum, String imgUrl, long point) {
        this.goalStatusIdx = goalStatusIdx;
        this.goalIdx = goalIdx;
        this.time = time;
        this.status = (byte)0;
        this.pillNum = pillNum;
        this.imgUrl = imgUrl;
        this.point = point;
    }
}
