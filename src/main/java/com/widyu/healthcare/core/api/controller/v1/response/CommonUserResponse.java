package com.widyu.healthcare.core.api.controller.v1.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.widyu.healthcare.core.domain.domain.v1.UserStatus;
import com.widyu.healthcare.core.domain.domain.v1.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 보호자 메인 화면
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class CommonUserResponse {
    private Long userIdx;
    private String name;
    private String profileImageUrl;
    @JsonIgnore
    private UserStatus status;
    private UserType userType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public long getUserIdx() {
        return userIdx == 0 ? null : userIdx;
    }
}
