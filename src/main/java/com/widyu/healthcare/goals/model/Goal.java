package com.widyu.healthcare.goals.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goal {

    private Long goalIdx;
    private Long userIdx;
    private String title;
    private String description;
    private Type type;
    private List<String> day;
    private String term;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<GoalStatus> goalStatusList;
}

enum Type {
    PILL,
    OUTDOOR
}