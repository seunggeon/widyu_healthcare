package com.widyu.healthcare.core.api.controller.v1.response.reward;

import com.widyu.healthcare.core.domain.domain.v1.RewardType;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class RewardResponse {

    @NonNull
    long rewardIdx;
    @NonNull
    long uploaderIdx; //uploader Idx
    @NonNull
    String uploaderName; //uploader name
    String uploaderImageUrl; //uploader의 profileImage
    String description;
    @NonNull
    RewardType type;
    String url;
    @NonNull
    private byte status; //closed:0, Open:1
    @NonNull
    long point;
}
