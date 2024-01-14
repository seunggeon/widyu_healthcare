package com.widyu.healthcare.dto.goals;

import com.google.firebase.database.annotations.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class Goal {

    private Long goalIdx;
    private Long userIdx;
    private String title;
    private String description;
    private Type type;
    private String day;
    private String term;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<GoalStatus> goalStatusList;
}

enum Type {
    PILL,
    OUTDOOR
}