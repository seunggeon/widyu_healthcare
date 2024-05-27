package com.widyu.healthcare.core.domain.domain.v1;

import lombok.Builder;

public class Location {
    private long userIdx;
    private double longitude;
    private double latitude;

    @Builder
    Location(long userIdx, double longitude, double latitude){
        this.userIdx = userIdx;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
