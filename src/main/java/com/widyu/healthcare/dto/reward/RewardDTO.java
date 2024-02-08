package com.widyu.healthcare.dto.reward;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RewardDTO {

    long rewardIdx;
    long userIdx;
    String url;
    String description;
}
