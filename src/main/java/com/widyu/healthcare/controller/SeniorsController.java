package com.widyu.healthcare.controller;

import com.widyu.healthcare.aop.LoginCheck;
import com.widyu.healthcare.dto.SuccessResponse;
import com.widyu.healthcare.service.SeniorsService;
import com.widyu.healthcare.utils.SessionUtil;
import com.widyu.healthcare.dto.users.UsersDTO;
import jakarta.servlet.http.HttpSession;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.codehaus.commons.nullanalysis.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Log4j2
@RestController
@RequestMapping("/api/v1/senior")
public class SeniorsController {
    @Autowired
    private SeniorsService seniorService;
    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody @NotNull UsersDTO userInfo) {
        if (UsersDTO.hasNullDataBeforeSeniorSignup(userInfo)) {
            throw new NullPointerException("회원가입 시 필수 데이터를 모두 입력해야 합니다.");
        }
        String inviteCode = seniorService.insertAndGetInviteCode(userInfo);
        SuccessResponse response = new SuccessResponse(true, "시니어 회원가 성공", inviteCode);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody @NonNull SeniorLoginRequest loginRequest, HttpSession session) {
        UsersDTO seniorInfo = seniorService.login(loginRequest.getInviteCode());
        if (UsersDTO.Status.ACTIVE.equals(seniorInfo.getStatus())) {
            SessionUtil.setLoginSeniorId(session, seniorInfo.getUserIdx());
            log.info("session 저장 성공", seniorInfo.getUserIdx());
        }
        SuccessResponse response = new SuccessResponse(true, "시니어 로그인 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("logout")
    @LoginCheck(type = LoginCheck.UserType.SENIOR)
    public ResponseEntity<?> logout(HttpSession session) {
        SessionUtil.logoutSenior(session);
        SuccessResponse response = new SuccessResponse(true, "시니어 로그아웃 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //************* Request ***********//
    @Getter
    private static class SeniorLoginRequest {
        @NonNull
        private String inviteCode;
    }

//
//    // 시니어 회원가입 : 이름, 출생년도, 연락처, 집주소 -> 생성된 시니어 아이디를 부양자 테이블에 id update
//
//    // 시니어 로그인 : 초대 코드 확인
//
//    // 부양자 아이디로 조회
//
//    // 아이디 찾기 : 이름 연락처 -> 아이디
//
//    // 비밀번호 찾기 : 아이디 -> 비번
//
//    // 시니어 추가 -> 회원가입 api로 대체
//
//    // 내 시니어 모두 조회
//
//    // 부양자 연락처 수정
}
