package com.widyu.healthcare.core.api.controller.v1;

import com.widyu.healthcare.core.domain.domain.v1.UserStatus;
import com.widyu.healthcare.core.api.controller.v1.request.senior.RegisterSeniorRequest;
import com.widyu.healthcare.core.api.controller.v1.response.SuccessResponse;
import com.widyu.healthcare.core.api.controller.v1.response.CommonUserResponse;
import com.widyu.healthcare.core.api.middleware.LoginCheck;
import com.widyu.healthcare.core.api.controller.v1.request.senior.LoginSeniorRequest;
import com.widyu.healthcare.core.api.controller.v1.response.FamilyInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.guardian.GuardianInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.request.senior.UpdateSeniorProfileRequest;
import com.widyu.healthcare.core.domain.service.v1.SeniorsService;
import com.widyu.healthcare.support.utils.SessionUtil;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/senior")
public class SeniorsController {
    private final SeniorsService seniorsService;
    @PostMapping("register/{guardianIdx}")
    public ResponseEntity<?> register(@PathVariable long guardianIdx, @RequestBody @Valid RegisterSeniorRequest seniorInfo) {
        String loginCode = seniorsService.insertAndSetRelations(guardianIdx, seniorInfo.toEncryptedUser());
        SuccessResponse response = new SuccessResponse(true, "시니어 회원가입 성공", loginCode);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginSeniorRequest loginReq, HttpSession session) {
        CommonUserResponse seniorInfo = seniorsService.loginByInviteCode(loginReq.getInviteCode(), loginReq.getFcmToken(), session );
        SuccessResponse response = new SuccessResponse(true, "시니어 로그인 성공", seniorInfo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("logout")
    @LoginCheck(type = LoginCheck.UserType.SENIOR)
    public ResponseEntity<?> logout(HttpSession apiUser) {
        SessionUtil.logoutSenior(apiUser);
        SuccessResponse response = new SuccessResponse(true, "시니어 로그아웃 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("info/with-guardians")
    @LoginCheck(type = LoginCheck.UserType.SENIOR)
    public ResponseEntity<?> getFamilyInfo(HttpSession apiUser) {
        FamilyInfoResponse guardiansAndTargetInfo = seniorsService.getGuardiansAndTargetInfo(SessionUtil.getLoginSeniorIdx(apiUser));
        SuccessResponse response = new SuccessResponse(true, "보호자 및 내 정보 조회 성공", guardiansAndTargetInfo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("find/guardians/{guardianId}")
    public ResponseEntity<?> getGuardianById(@PathVariable String guardianId) {
        GuardianInfoResponse guardianInfo = seniorsService.findGuardianByGuardianId(guardianId);
        SuccessResponse response = new SuccessResponse(true, "찾고자 하는 보호자 ID 조회 성공", guardianInfo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("add/{guardianIdx}")
    public ResponseEntity<?> addGuardian(@PathVariable long guardianIdx, HttpSession apiUser) {
        seniorsService.addGuardian(guardianIdx, SessionUtil.getLoginSeniorIdx(apiUser));
        SuccessResponse response = new SuccessResponse(true, "보호자 추가 등록 성공", null);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("profile")
    public ResponseEntity<?> updateProfile(@RequestBody @Valid UpdateSeniorProfileRequest profileReq, HttpSession apiUser) {
        seniorsService.updateProfile(SessionUtil.getLoginSeniorIdx(apiUser), profileReq.toUser());
        SuccessResponse response = new SuccessResponse(true, "시니어 프로릴 수정 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
