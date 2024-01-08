package com.widyu.healthcare.dto.goals;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalStatus {

    private LocalDateTime time;
    private int status;
    private int pillNum;
    private String ImgUrl;
    private Long point;
}
