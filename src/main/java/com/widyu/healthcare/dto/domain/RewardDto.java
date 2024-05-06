package com.widyu.healthcare.dto.domain;

import com.widyu.healthcare.dto.RewardType;
import lombok.Data;
import lombok.NonNull;

@Data
public class RewardDto {

    long rewardIdx;
    @NonNull
    long userIdx;
    String description;
    RewardType type;
    String url;
    private byte status; //closed:0, Open:1
    long point;

    public RewardDto(long userIdx, String description, String url) {
        this.userIdx = userIdx;
        this.url = url;
        this.description = description;
        this.status = 0;
        this.point = 30;
    }

}