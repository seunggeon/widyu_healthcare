package com.widyu.healthcare.users;

import com.widyu.healthcare.global.aop.LoginCheck;
import com.widyu.healthcare.global.utils.SessionUtil;
import com.widyu.healthcare.users.dto.UsersDTO;
import com.widyu.healthcare.users.response.LoginResponse;
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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity signUp(@RequestBody @NotNull UsersDTO userInfo) {
        ResponseEntity responseEntity = null;
        if (UsersDTO.hasNullDataBeforeSeniorSignup(userInfo)) {
            throw new NullPointerException("회원가입시 필수 데이터를 모두 입력해야 합니다.");
        }
        String inviteCode = seniorService.insertAndGetInviteCode(userInfo);
        if (inviteCode != null) {
            responseEntity = new ResponseEntity(inviteCode, HttpStatus.OK);
        }
        else {
            responseEntity = new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody @NonNull SeniorLoginRequest loginRequest, HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String inviteCode = loginRequest.getInviteCode();
        LoginResponse loginResponse;
        UsersDTO userInfo = seniorService.login(inviteCode);
        if (userInfo == null) {
            loginResponse = LoginResponse.FAIL;
            responseEntity = new ResponseEntity<LoginResponse>(loginResponse,
                    HttpStatus.UNAUTHORIZED);
        } else if (UsersDTO.Status.ACTIVE.equals(userInfo.getStatus())) {
            loginResponse = LoginResponse.success(userInfo);
            SessionUtil.setLoginSeniorId(session, userInfo.getInviteCode());
            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } else {
            log.error("login Senior ERROR" + responseEntity);
            throw new RuntimeException("login Senior ERROR!");
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

//    // 문자 인증 번호 보내기
//
//    // 문자 인증 번호 확인 및 부양자 중복 회원 체크 유무
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
