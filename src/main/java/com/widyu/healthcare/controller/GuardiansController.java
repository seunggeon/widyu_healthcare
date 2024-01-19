package com.widyu.healthcare.controller;

import com.google.firebase.database.annotations.NotNull;
import com.widyu.healthcare.aop.LoginCheck;
import com.widyu.healthcare.aop.LoginCheck.UserType;
import com.widyu.healthcare.dto.SuccessResponse;
import com.widyu.healthcare.error.exception.DuplicateIdException;
import com.widyu.healthcare.service.GuardiansService;
import jakarta.servlet.http.HttpSession;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.widyu.healthcare.dto.users.UsersDTO;
import com.widyu.healthcare.utils.SessionUtil;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/guardian")
public class GuardiansController {
    @Autowired
    private GuardiansService guardianService;
    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody @NotNull UsersDTO userInfo) {
        if (UsersDTO.hasNullDataBeforeGuardianSignup(userInfo)) {
            log.error("insert Senior ERROR! {}", userInfo);
            throw new NullPointerException("회원가입 시 필수 데이터를 모두 입력해야 합니다.");
        }
        guardianService.insert(userInfo);
        SuccessResponse response = new SuccessResponse(true, "보호자 회원가입 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody @NonNull GuardianLoginRequest loginRequest,
                                               HttpSession session) {
        UsersDTO guardianInfo = guardianService.loginByIdAndPassword(loginRequest.getId(), loginRequest.getPassword());
        if (UsersDTO.Status.ACTIVE.equals(guardianInfo.getStatus())) {
            SessionUtil.setLoginGuardianId(session, guardianInfo.getUserIdx());
            log.info("session 저장 성공", guardianInfo.getUserIdx());
        }
        SuccessResponse response = new SuccessResponse(true, "보호자 로그인 성공", guardianInfo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("logout")
    @LoginCheck(type = UserType.GUARDIAN)
    public ResponseEntity<?> logout(HttpSession session) {
        SessionUtil.logoutGuardian(session);
        SuccessResponse response = new SuccessResponse(true, "보호자 로그아웃 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("info/seniors")
    @LoginCheck(type = UserType.GUARDIAN)
    public ResponseEntity<?> getAllSeniors(HttpSession session) {
        List<UsersDTO> seniorsInfo = guardianService.getSeniorsOrNull(SessionUtil.getLoginGuardianId(session));
        SuccessResponse response = new SuccessResponse(true, "보호자의 모든 시니어 조회", seniorsInfo);

        return new ResponseEntity<>(response, HttpStatus.OK);
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
