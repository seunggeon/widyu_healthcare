package com.widyu.healthcare.dto.goals;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GoalSetDTO {

    private GoalDTO goalDTO;
    private List<GoalStatusDTO> goalStatusDTOList;
}
