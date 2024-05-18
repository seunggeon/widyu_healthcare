package com.widyu.healthcare.core.api.controller.v1.response.reward;

import com.widyu.healthcare.core.domain.domain.v1.RewardType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
public class RewardResponse {

    @NonNull
    long rewardIdx;
    @NonNull
    long uploaderIdx; //uploader Idx
    String description;
    @NonNull
    RewardType type;
    String url;
    @NonNull
    private byte status; //closed:0, Open:1
    @NonNull
    long point;
    String profileImageUrl; //uploader의 profileImage
}
