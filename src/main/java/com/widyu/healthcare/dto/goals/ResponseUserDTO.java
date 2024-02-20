package com.widyu.healthcare.dto.goals;

import com.widyu.healthcare.aop.LoginCheck;
import lombok.Data;

import java.util.List;

@Data
public class ResponseUserDTO {

    String name;
    String profileImageUrl;
    int percentage;
    LoginCheck.UserType userType;
    List<GoalSetDTO> goals;

    //부모님일 경우 가족 순번도 반환

    //부모님일 경우 보유 포인트도 반환
}