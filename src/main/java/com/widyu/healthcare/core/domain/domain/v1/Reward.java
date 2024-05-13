package com.widyu.healthcare.core.domain.domain.v1;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class Reward {

    long rewardIdx;
    @NonNull
    long userIdx;
    String description;
    RewardType type;
    String url;
    private byte status; //closed:0, Open:1
    long point;
    @Builder
    public Reward(long userIdx, String description, String url) {
        this.userIdx = userIdx;
        this.url = url;
        this.description = description;
        this.status = 0;
        this.point = 30;
    }

}