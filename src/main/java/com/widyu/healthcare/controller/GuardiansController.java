package com.widyu.healthcare.controller;

import com.widyu.healthcare.aop.LoginCheck;
import com.widyu.healthcare.aop.LoginCheck.UserType;
import com.widyu.healthcare.dto.response.SuccessResponse;
import com.widyu.healthcare.dto.UserStatus;
import com.widyu.healthcare.dto.request.*;
import com.widyu.healthcare.dto.response.*;
import com.widyu.healthcare.service.GuardiansService;
import com.widyu.healthcare.service.SeniorsService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.widyu.healthcare.utils.SessionUtil;

@Log4j2
@RestController
@RequestMapping("/api/v1/guardian")
public class GuardiansController {
    @Autowired
    private GuardiansService guardiansService;
    @Autowired
    private SeniorsService seniorsService;
    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody @Valid GuardianRequestDto userInfo) {
        guardiansService.insert(userInfo);
        SuccessResponse response = new SuccessResponse(true, "보호자 회원가입 성공", null);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody @Valid GuardianLoginRequestDto loginRequest,
                                               HttpSession session) {
        UsersResponseDto guardianInfo = guardiansService.loginByIdAndPassword(loginRequest.getId(), loginRequest.getPassword());
        if (UserStatus.ACTIVE.equals(guardianInfo.getStatus())) {
            guardiansService.updateFCM(guardianInfo.getUserIdx(), loginRequest.getFcmToken());
            SessionUtil.setLoginGuardianId(session, guardianInfo.getUserIdx());
            log.info("session 저장 성공", guardianInfo.getUserIdx());
        }
        SuccessResponse response = new SuccessResponse(true, "보호자 로그인 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("logout")
    @LoginCheck(type = UserType.GUARDIAN)
    public ResponseEntity<?> logout(HttpSession session) {
        SessionUtil.logoutGuardian(session);
        SuccessResponse response = new SuccessResponse(true, "보호자 로그아웃 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("find/id")
    public ResponseEntity<?> findId(@RequestBody @Valid GuardianFindIdRequestDto findIdRequest) {
        GuardianDetailResponseDto guardianId = guardiansService.findId(findIdRequest.getName(), findIdRequest.getPhoneNumber());
        SuccessResponse response = new SuccessResponse(true, "보호자 아이디 찾기 성공", guardianId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("find/password")
    public ResponseEntity<?> findPassword(@RequestBody @Valid GuardianFindPasswordRequestDto findPasswordRequest) {
        GuardianDetailResponseDto guardianInfo = guardiansService.findPassword(findPasswordRequest.getId(), findPasswordRequest.getName(), findPasswordRequest.getPhoneNumber());
        SuccessResponse response = new SuccessResponse(true, "보호자 비밀번호 찾기 성공", guardianInfo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("info/seniors")
    @LoginCheck(type = UserType.GUARDIAN)
    public ResponseEntity<?> getSeniorsAndMyInfo(HttpSession session) {
        FamilyResponseDto seniorsAndMyInfo = guardiansService.getSeniorsAndMyInfo(SessionUtil.getLoginGuardianId(session));
        SuccessResponse response = new SuccessResponse(true, "보호자의 모든 시니어 조회", seniorsAndMyInfo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping ("add/seniors")
    @LoginCheck(type = UserType.GUARDIAN)
    public ResponseEntity<?> addSeniors(@RequestBody SeniorRequestDto seniorRequest, HttpSession session) {
        seniorsService.insertAndSetRelations(SessionUtil.getLoginGuardianId(session), seniorRequest);
        SuccessResponse response = new SuccessResponse(true, "시니어 추가 등록", null);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 보호자 프로필 수정 (일부)
     * @param profileRequest
     * @param session
     * @return
     */
    @PatchMapping ("profile")
    @LoginCheck(type = UserType.GUARDIAN)
    public ResponseEntity<?> updateProfile(@RequestBody SeniorProfileRequestDto profileRequest, HttpSession session) {
        guardiansService.updateProfile(SessionUtil.getLoginGuardianId(session), profileRequest.getName(), profileRequest.getPhoneNumber());
        SuccessResponse response = new SuccessResponse(true, "보호자의 프로필 일부 수정 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
