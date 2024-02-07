package com.widyu.healthcare.dto.reward;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RewardDTO {

    long userIdx;
    String url;
    String description;
}
