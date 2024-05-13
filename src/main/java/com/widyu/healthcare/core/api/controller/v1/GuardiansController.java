package com.widyu.healthcare.core.api.controller.v1;

import com.widyu.healthcare.core.api.controller.v1.request.guardian.FindGuardianIdRequest;
import com.widyu.healthcare.core.api.controller.v1.request.guardian.FindGuardianPasswordRequest;
import com.widyu.healthcare.core.api.controller.v1.request.senior.RegisterSeniorRequest;
import com.widyu.healthcare.core.api.controller.v1.request.guardian.LoginGuardianRequest;
import com.widyu.healthcare.core.api.controller.v1.request.guardian.UpdateGuardianProfileRequest;
import com.widyu.healthcare.core.api.controller.v1.request.guardian.RegisterGuardianRequest;

import com.widyu.healthcare.core.api.controller.v1.response.SuccessResponse;
import com.widyu.healthcare.core.api.controller.v1.response.CommonUserResponse;
import com.widyu.healthcare.core.api.controller.v1.response.FamilyInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.guardian.GuardianInfoResponse;

import com.widyu.healthcare.core.api.middleware.LoginCheck;
import com.widyu.healthcare.core.api.middleware.LoginCheck.UserType;
import com.widyu.healthcare.core.domain.service.v1.GuardiansService;
import com.widyu.healthcare.core.domain.service.v1.SeniorsService;

import com.widyu.healthcare.support.utils.SHA256Util;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.widyu.healthcare.support.utils.SessionUtil;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guardian")
public class GuardiansController {
    private GuardiansService guardiansService;
    private SeniorsService seniorsService;
    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterGuardianRequest guardianReq) {
        GuardianInfoResponse userInfo = guardiansService.insert(guardianReq.toEncryptedUser());
        SuccessResponse response = new SuccessResponse(true, "보호자 회원가입 성공", userInfo);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginGuardianRequest loginReq,
                                               HttpSession session) {
        CommonUserResponse guardianInfo = guardiansService.loginByIdAndPassword(loginReq.getId(), SHA256Util.encryptSHA256(loginReq.getPassword()), loginReq.getFcmToken(), session);
        SuccessResponse response = new SuccessResponse(true, "보호자 로그인 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("logout")
    @LoginCheck(type = UserType.GUARDIAN)
    public ResponseEntity<?> logout(HttpSession apiUser) {
        SessionUtil.logoutGuardian(apiUser);
        SuccessResponse response = new SuccessResponse(true, "보호자 로그아웃 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("find/id")
    public ResponseEntity<?> findId(@RequestBody @Valid FindGuardianIdRequest findIdReq) {
        GuardianInfoResponse guardianId = guardiansService.findId(findIdReq.getName(), findIdReq.getPhoneNumber());
        SuccessResponse response = new SuccessResponse(true, "보호자 아이디 찾기 성공", guardianId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("find/password")
    public ResponseEntity<?> findPassword(@RequestBody @Valid FindGuardianPasswordRequest findPasswordReq) {
        GuardianInfoResponse guardianInfo = guardiansService.findPassword(findPasswordReq.getId(), findPasswordReq.getNewPassword(), findPasswordReq.getName(), findPasswordReq.getPhoneNumber());
        SuccessResponse response = new SuccessResponse(true, "보호자 비밀번호 찾기 성공", guardianInfo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("info/with-seniors")
    @LoginCheck(type = UserType.GUARDIAN)
    public ResponseEntity<?> targetAndSeniorsInfo(HttpSession apiUser) {
        long targetIdx = SessionUtil.getLoginGuardianIdx(apiUser);
        FamilyInfoResponse targetAndSeniorsInfo = guardiansService.getSeniorsAndMyInfo(targetIdx);
        SuccessResponse response = new SuccessResponse(true, "보호자의 모든 시니어 조회 성공", targetAndSeniorsInfo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping ("add/more-seniors")
    @LoginCheck(type = UserType.GUARDIAN)
    public ResponseEntity<?> addMoreSeniors(@RequestBody RegisterSeniorRequest seniorReq, HttpSession apiUser) {
        seniorsService.insertAndSetRelations(SessionUtil.getLoginGuardianIdx(apiUser), seniorReq.toEncryptedUser());
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
    public ResponseEntity<?> updateProfile(@RequestBody UpdateGuardianProfileRequest profileRequest, HttpSession session) {
        guardiansService.updateProfile(SessionUtil.getLoginGuardianIdx(session), profileRequest.toUser());
        SuccessResponse response = new SuccessResponse(true, "보호자의 프로필 수정 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
