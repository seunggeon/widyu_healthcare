package com.widyu.healthcare.core.api.controller.v1.response.health;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GuardianMainHealthResponse {
    private List<SeniorMainHealthResponse> seniorMainHealthResponseList;

    @Builder
    public GuardianMainHealthResponse(List<SeniorMainHealthResponse> seniorMainHealthResponseList){
        this.seniorMainHealthResponseList = seniorMainHealthResponseList;
    }
}
