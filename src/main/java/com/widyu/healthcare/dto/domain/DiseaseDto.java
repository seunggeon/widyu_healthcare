package com.widyu.healthcare.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
public class DiseaseDto {
    private long diseaseIdx;
    @JsonProperty("name")
    private String diseaseName;
    private String drugName;
    private String explanation;
    private String favoriteHospital;
    private String dosageTime;
}