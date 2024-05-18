package com.widyu.healthcare.core.domain.domain.v1;

import jakarta.annotation.Nullable;
import lombok.*;

import java.sql.Time;

@Getter
@NoArgsConstructor
public class GoalStatus {

    private long goalStatusIdx;
    @Nullable
    private long goalIdx;
    @NonNull
    private Time time;
    @NonNull
    private int status;
    private Long pillNum;
    private String imgUrl;
    private long point;
    @Builder
    public GoalStatus(long goalStatusIdx, long goalIdx, @NonNull Time time, int status, Long pillNum, String imgUrl, long point) {
        this.goalStatusIdx = goalStatusIdx;
        this.goalIdx = goalIdx;
        this.time = time;
        this.status = status;
        this.pillNum = pillNum;
        this.imgUrl = imgUrl;
        this.point = point;
    }

    public void setGoalIdx(long goalIdx){
        this.goalIdx = goalIdx;
    }
}
