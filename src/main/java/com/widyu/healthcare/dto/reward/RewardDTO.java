package com.widyu.healthcare.dto.reward;

import lombok.Data;

@Data
public class RewardDTO {

    long rewardIdx;
    long userIdx;
    String url;
    String description;
}
