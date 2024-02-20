package com.widyu.healthcare.dto.reward;

import lombok.Data;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RewardDTO {

    long rewardIdx;
    @NonNull
    long userIdx;
    String description;
    private byte status;
    String url;
    long point;

    public RewardDTO(long userIdx, String description, String url) {
        this.userIdx = userIdx;
        this.url = url;
        this.description = description;
        this.status = 0;
        this.point = 1;
    }
}