package com.widyu.healthcare.core.api.controller.v1.request.health;

import com.widyu.healthcare.core.domain.domain.v1.Location;
import jakarta.annotation.Nullable;
import lombok.Getter;

/**
 *
 */
@Getter
public class UpdateSeniorLocationRequest {
    @Nullable
    private double longitude;
    @Nullable
    private double latitude;

    public Location toLocation() {
        Location location = Location.builder()
                .longitude(longitude)
                .latitude(latitude)
                .build();
        return location;
    }
}
