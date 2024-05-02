package com.widyu.healthcare.dto.domain;

import lombok.Data;
import lombok.NonNull;

@Data
public class RewardDto {

    long rewardIdx;
    @NonNull
    long userIdx;
    String description;
    private byte status;
    String url;
    long point;

    public RewardDto(long userIdx, String description, String url) {
        this.userIdx = userIdx;
        this.url = url;
        this.description = description;
        this.status = 0;
        this.point = 30;
    }

}