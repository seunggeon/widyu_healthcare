package com.widyu.healthcare.controller;

import com.google.firebase.database.annotations.NotNull;
import com.widyu.healthcare.aop.LoginCheck;
import com.widyu.healthcare.aop.LoginCheck.UserType;
import com.widyu.healthcare.controller.response.LoginResponse;
import com.widyu.healthcare.service.GuardianService;
import jakarta.servlet.http.HttpSession;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.widyu.healthcare.dto.users.UsersDTO;
import com.widyu.healthcare.utils.SessionUtil;
@Log4j2
@RestController
@RequestMapping("/guardian")
public class GuardianController {
    @Autowired
    private GuardianService guardianService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody @NotNull UsersDTO userInfo) {
        if (UsersDTO.hasNullDataBeforeGuardianSignup(userInfo)) {
            throw new NullPointerException("회원가입 시 필수 데이터를 모두 입력해야 합니다.");
        }
        guardianService.insert(userInfo);
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody @NonNull GuardianLoginRequest loginRequest,
                                               HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String id = loginRequest.getId();
        String password = loginRequest.getPassword();
        LoginResponse loginResponse;
        UsersDTO userInfo = guardianService.login(id, password);

        if (userInfo == null) {
            loginResponse = LoginResponse.FAIL;
            responseEntity = new ResponseEntity<LoginResponse>(loginResponse,
                    HttpStatus.UNAUTHORIZED);
        } else if (UsersDTO.Status.ACTIVE.equals(userInfo.getStatus())) {
            loginResponse = LoginResponse.success(userInfo);
            SessionUtil.setLoginGuardianId(session, id);
            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } else {
            log.error("login 부양자 ERROR" + responseEntity);
            throw new RuntimeException("login 부양자 ERROR!");
        }
        return responseEntity;
    }
    @GetMapping("logout")
    @LoginCheck(type = UserType.GUARDIAN)
    public void logout(HttpSession session) {
        SessionUtil.logoutGuardian(session);
    }

    //************* Request ***********//
    @Setter
    @Getter
    private static class GuardianLoginRequest {
        @NonNull
        private String id;
        @NonNull
        private String password;
    }
//
//    // 시니어 회원가입 : 이름, 출생년도, 연락처, 집주소 -> 생성된 시니어 아이디를 부양자 테이블에 id update
//
//    // 부양자 아이디로 조회
//
//    // 아이디 찾기 : 이름 연락처 -> 아이디
//
//    // 비밀번호 찾기 : 아이디 -> 비번
//
//    // 내 시니어 모두 조회
//
//    // 부양자 연락처 수정
}
