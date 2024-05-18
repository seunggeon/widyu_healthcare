package com.widyu.healthcare.core.domain.domain.v1;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Disease {
    private Long diseaseIdx;
    private String name;
    private String drugName;
    private String explanation;
    private String favoriteHospital;
    private String dosageTime;

    @Builder
    public Disease(Long diseaseIdx, String name, String drugName, String explanation, String favoriteHospital, String dosageTime){
        this.diseaseIdx = diseaseIdx;
        this.name = name;
        this.drugName = drugName;
        this.explanation = explanation;
        this.favoriteHospital = favoriteHospital;
        this.dosageTime = dosageTime;
    }
}