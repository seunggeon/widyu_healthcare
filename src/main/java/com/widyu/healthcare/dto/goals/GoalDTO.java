package com.widyu.healthcare.dto.goals;

import lombok.*;

import java.sql.Timestamp;
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
    private Timestamp startDate;
    private Timestamp endDate;
}

enum Type {
    PILL,
    OUTDOOR
}