package com.widyu.healthcare.dto.goals;

import lombok.*;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Getter
@Setter
@AllArgsConstructor
public class GoalStatus {

    private Long goalIdx;
    private LocalTime time;
    private int status;
    private int pillNum;
    private String ImgUrl;
    private Long point;
}
