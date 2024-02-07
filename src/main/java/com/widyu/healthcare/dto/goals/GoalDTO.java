package com.widyu.healthcare.dto.goals;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class GoalDTO {

    private Long goalIdx;
    private Long userIdx;
    private String title;
    private String description;
    private Type type;
    private String day;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<GoalStatus> goalStatusList;
}

enum Type {
    PILL,
    OUTDOOR
}