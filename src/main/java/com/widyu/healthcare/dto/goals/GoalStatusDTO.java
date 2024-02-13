package com.widyu.healthcare.dto.goals;

import com.google.firebase.database.annotations.NotNull;
import lombok.*;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Getter
@Setter
@AllArgsConstructor
public class GoalStatusDTO {

    private Long goalStatusIdx;
    private Long goalIdx;
    @NonNull
    private Time time;
    private Integer status;
    private Long pillNum;
    private String imgUrl;
    private Long point;
}
