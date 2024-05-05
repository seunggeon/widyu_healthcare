package com.widyu.healthcare.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.widyu.healthcare.dto.UserStatus;
import com.widyu.healthcare.dto.UserType;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 보호자 메인 화면
 */
@Getter
@Setter
@SuperBuilder
// 없으면 mapper.xml resultType에 할당이 안됨.
@NoArgsConstructor
public class UsersResponseDto {
    private long userIdx;
    private String name;
    private String profileImageUrl;
    @JsonIgnore
    private UserStatus status;
    private UserType userType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long getUserIdx() {
        return userIdx == 0 ? null : userIdx;
    }
}
