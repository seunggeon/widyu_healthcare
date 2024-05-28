package com.widyu.healthcare.core.domain.domain.v1;

import lombok.*;

@Getter
@NoArgsConstructor
public class Reward {

    long rewardIdx;
    @NonNull
    long userIdx; //target Idx
    @NonNull
    long uploaderIdx; //uploader Idx
    String description;
    @NonNull
    RewardType type;
    @NonNull
    String url;
    private byte status; //closed:0, Open:1
    long point;
    String profileImageUrl; //uploader의 profileImage
    @Builder
    public Reward(long userIdx, long uploaderIdx, String description, String url, RewardType type) {
        this.userIdx = userIdx;
        this.uploaderIdx = uploaderIdx;
        this.url = url;
        this.description = description;
        this.type = type;
        this.status = 0;
        this.point = 30;
    }

    public void setUserIdx(long userIdx){
        this.userIdx = userIdx;
    }
}