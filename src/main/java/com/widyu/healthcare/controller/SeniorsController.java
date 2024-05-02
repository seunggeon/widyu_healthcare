package com.widyu.healthcare.controller;

import com.widyu.healthcare.aop.LoginCheck;
import com.widyu.healthcare.dto.response.SuccessResponse;
import com.widyu.healthcare.dto.UserStatus;
import com.widyu.healthcare.dto.request.SeniorLoginRequestDto;
import com.widyu.healthcare.dto.request.SeniorProfileRequestDto;
import com.widyu.healthcare.dto.request.SeniorRequestDto;
import com.widyu.healthcare.dto.response.*;
import com.widyu.healthcare.service.SeniorsService;
import com.widyu.healthcare.utils.SessionUtil;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Log4j2
@RestController
@RequestMapping("/api/v1/senior")
public class SeniorsController {
    @Autowired
    private SeniorsService seniorsService;
    @PostMapping("register/{guardianIdx}")
    public ResponseEntity<?> register(@PathVariable long guardianIdx, @RequestBody @Valid SeniorRequestDto seniorRegister) {
        String loginCode = seniorsService.insertAndSetRelations(guardianIdx, seniorRegister);
        // Data 에는 DB에서 받아온 DTO 객체로 넣기
        SuccessResponse response = new SuccessResponse(true, "시니어 회원가입 성공", loginCode);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody @Valid SeniorLoginRequestDto loginRequest, HttpSession session) {
        UsersResponseDto seniorInfo = seniorsService.loginByInviteCode(loginRequest.getInviteCode());
        if (UserStatus.ACTIVE.equals(seniorInfo.getStatus())) {
            seniorsService.updateFCM(seniorInfo.getUserIdx(), loginRequest.getFcmToken());
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

    @GetMapping("info/guardians")
    @LoginCheck(type = LoginCheck.UserType.SENIOR)
    public ResponseEntity<?> getFamilyInfo(HttpSession session) {
        FamilyResponseDto guardiansAndMyInfo = seniorsService.getGuardiansAndMyinfo(SessionUtil.getLoginSeniorId(session));
        SuccessResponse response = new SuccessResponse(true, "보호자 및 내 정보 조회 성공", guardiansAndMyInfo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("find/guardians/{guardianId}")
    public ResponseEntity<?> getGuardianById(@PathVariable String guardianId) {
        GuardianDetailResponseDto guardianInfo = seniorsService.findGuardianByGuardianId(guardianId);
        SuccessResponse response = new SuccessResponse(true, "찾고자 하는 보호자 ID 조회 성공", guardianInfo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("add/{guardianIdx}")
    public ResponseEntity<?> addGuardian(@PathVariable long guardianIdx, HttpSession session) {
        seniorsService.addGuardian(guardianIdx, SessionUtil.getLoginSeniorId(session));
        SuccessResponse response = new SuccessResponse(true, "보호자 추가 등록 성공", null);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("profile")
    public ResponseEntity<?> updateProfile(@RequestBody @Valid SeniorProfileRequestDto profileRequest, HttpSession session) {
        seniorsService.updateProfile(SessionUtil.getLoginSeniorId(session), profileRequest);
        SuccessResponse response = new SuccessResponse(true, "시니어 프로릴 수정 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
