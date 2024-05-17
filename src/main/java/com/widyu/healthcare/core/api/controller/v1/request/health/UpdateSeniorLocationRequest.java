package com.widyu.healthcare.core.api.controller.v1.request.health;

import com.widyu.healthcare.core.domain.domain.v1.Disease;
import com.widyu.healthcare.core.domain.domain.v1.Health;
import com.widyu.healthcare.core.domain.domain.v1.User;
import jakarta.annotation.Nullable;
import lombok.Getter;

import java.util.List;

import static com.widyu.healthcare.core.domain.domain.v1.UserType.SENIOR;

/**
 * 시니어 프로필 수정 (일부)
 * @param
 */
@Getter
public class UpdateSeniorLocationRequest {
    @Nullable
    private double latitude;
    @Nullable
    private double longitude;

    public Health toHealth() {
        Health health = Health.builder()
                .longitude(this.longitude)
                .latitude(this.latitude)
                .build();
        return health;
    }
}
