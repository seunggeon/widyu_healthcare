package com.widyu.healthcare.core.domain.domain.v1;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Disease {
    private Long diseaseIdx;
    @Nullable
    private String name;
    @Nullable
    private String drugName;
    @Nullable
    private String explanation;
    @Nullable
    private String favoriteHospital;
    @Nullable
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