package com.widyu.healthcare.core.domain.domain.v1;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class UserDetail {
    private String profileImageUrl;
    @Nullable
    private String id;
    @Nullable
    private String password;
    @Nullable
    private String fcmToken;
}
