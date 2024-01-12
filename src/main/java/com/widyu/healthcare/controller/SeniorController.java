package com.widyu.healthcare.controller;

import com.widyu.healthcare.aop.LoginCheck;
import com.widyu.healthcare.service.SeniorService;
import com.widyu.healthcare.utils.SessionUtil;
import com.widyu.healthcare.dto.users.UsersDTO;
import com.widyu.healthcare.controller.response.LoginResponse;
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
@RequestMapping("/senior")
public class SeniorController {
    @Autowired
    private SeniorService seniorService;
    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity signUp(@RequestBody @NotNull UsersDTO userInfo) {
        ResponseEntity responseEntity = null;

        if (UsersDTO.hasNullDataBeforeSeniorSignup(userInfo)) {
            throw new NullPointerException("회원가입 시 필수 데이터를 모두 입력해야 합니다.");
        }
        String inviteCode = seniorService.insertAndGetInviteCode(userInfo);
        if (inviteCode != null) {
            responseEntity = new ResponseEntity(inviteCode, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    @PostMapping("login")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<LoginResponse> login(@RequestBody @NonNull SeniorLoginRequest loginRequest, HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        LoginResponse loginResponse = null;
        String inviteCode = loginRequest.getInviteCode();

        UsersDTO userInfo = seniorService.login(inviteCode);
        if (userInfo == null) { // 회원정보가 없음
            loginResponse = LoginResponse.FAIL;
            responseEntity = new ResponseEntity<LoginResponse>(loginResponse,
                    HttpStatus.UNAUTHORIZED);
        } else if (UsersDTO.Status.ACTIVE.equals(userInfo.getStatus())) { // 회원 정보가 존재
            loginResponse = LoginResponse.success(userInfo);
            SessionUtil.setLoginSeniorId(session, userInfo.getUserIdx());
            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } else {
            loginResponse = LoginResponse.DELETED;
            responseEntity = new ResponseEntity<LoginResponse>(loginResponse,
                    HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }
    @GetMapping("logout")
    @LoginCheck(type = LoginCheck.UserType.SENIOR)
    public void logout(HttpSession session) {
        SessionUtil.logoutSenior(session);
    }

    //************* Request ***********//
    @Setter
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
