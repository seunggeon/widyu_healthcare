package com.widyu.healthcare.core.domain.domain.v1;

import jakarta.annotation.Nullable;
import lombok.*;

import java.sql.Time;

import static com.widyu.healthcare.support.config.AppConfig.GOAL_POINT;

@Getter
@NoArgsConstructor
public class GoalStatus {

    private long goalStatusIdx;
    @Nullable
    private long goalIdx;
    @NonNull
    private Time time;
    @Nullable
    private int status;
    private Long pillNum;
    @Nullable
    private String imgUrl;
    private long point;
    @Builder
    public GoalStatus(long goalStatusIdx, long goalIdx, @NonNull Time time, Long pillNum, String imgUrl, long point) {

        this.goalStatusIdx = goalStatusIdx;
        this.goalIdx = goalIdx;
        this.time = time;
        this.status = 0;
        this.pillNum = pillNum;
        this.imgUrl = imgUrl;
        this.point = GOAL_POINT;
    }

    public void setGoalIdx(long goalIdx){
        this.goalIdx = goalIdx;
    }
}
