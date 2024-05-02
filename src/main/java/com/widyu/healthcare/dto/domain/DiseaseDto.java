package com.widyu.healthcare.dto.domain;

import lombok.Data;
@Data
public class DiseaseDto {
    private long diseaseIdx;
    private String name;
    private String drugName;
    private String explanation;
}