package com.widyu.healthcare.core.api.controller.v1;

import com.widyu.healthcare.core.api.controller.v1.request.guardian.FindGuardianIdRequest;
import com.widyu.healthcare.core.api.controller.v1.request.guardian.FindGuardianPasswordRequest;
import com.widyu.healthcare.core.api.controller.v1.request.senior.RegisterSeniorRequest;
import com.widyu.healthcare.core.api.controller.v1.request.guardian.LoginGuardianRequest;
import com.widyu.healthcare.core.api.controller.v1.request.guardian.UpdateGuardianProfileRequest;
import com.widyu.healthcare.core.api.controller.v1.request.guardian.RegisterGuardianRequest;
import com.widyu.healthcare.core.api.controller.v1.request.senior.UpdateSeniorProfileRequest;
import com.widyu.healthcare.core.api.controller.v1.response.FamilyIdxResponse;
import com.widyu.healthcare.core.api.controller.v1.response.SuccessResponse;
import com.widyu.healthcare.core.api.controller.v1.response.FamilyInfoResponse;
import com.widyu.healthcare.core.api.controller.v1.response.guardian.GuardianInfoResponse;
import com.widyu.healthcare.core.api.middleware.LoginCheck;
import com.widyu.healthcare.core.api.middleware.LoginCheck.UserType;
import com.widyu.healthcare.core.domain.service.v1.GuardiansService;
import com.widyu.healthcare.core.domain.service.v1.SeniorsService;
import com.widyu.healthcare.support.utils.SessionUtil;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import com.google.firebase.database.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guardian")
public class GuardiansController {
    private final GuardiansService guardiansService;
    private final SeniorsService seniorsService;

    /**
     * 보호자 회원가입
     * @author seunggeon
     * @breif 보호자 회원가입
     * @see
     * @param guardianReq
     * @return GuardianInfoResponse
     */
    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterGuardianRequest guardianReq) {
        GuardianInfoResponse userInfo = guardiansService.insert(guardianReq.toEncryptedUser());
        SuccessResponse response = new SuccessResponse(true, "보호자 회원가입 성공", userInfo);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 보호자 로그인
     * @param loginReq
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginGuardianRequest loginReq,
                                               HttpSession session) {
        guardiansService.loginByIdAndPassword(loginReq.toUserDetail(), session);
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
    /**
     * 보호자 아이디 찾기
     * @param findIdReq
     * @return
     */
    @PostMapping("find/id")
    public ResponseEntity<?> findId(@RequestBody @Valid FindGuardianIdRequest findIdReq) {
        GuardianInfoResponse guardianId = guardiansService.findId(findIdReq.getName(), findIdReq.getPhoneNumber());
        SuccessResponse response = new SuccessResponse(true, "보호자 아이디 찾기 성공", guardianId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 보호자 패스워드 찾기
     * @param findPasswordReq
     * @return
     */
    @PostMapping("find/password")
    public ResponseEntity<?> findPassword(@RequestBody @Valid FindGuardianPasswordRequest findPasswordReq) {
        guardiansService.findPassword(findPasswordReq.getId(), findPasswordReq.getNewPassword(), findPasswordReq.getName(), findPasswordReq.getPhoneNumber());
        SuccessResponse response = new SuccessResponse(true, "보호자 비밀번호 재설정 성공",null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("info/with-seniors")
    @LoginCheck(type = UserType.GUARDIAN)
    public ResponseEntity<?> targetAndSeniorsInfo(HttpSession apiUser) {
        long targetIdx = SessionUtil.getLoginGuardianIdx(apiUser);
        FamilyInfoResponse targetAndSeniorsInfo = guardiansService.getSeniorsAndTargetInfo(targetIdx);
        SuccessResponse response = new SuccessResponse(true, "보호자의 모든 시니어 조회 성공", targetAndSeniorsInfo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("index/of-family")
    @LoginCheck(type = UserType.GUARDIAN)
    public ResponseEntity<?> seniorsIdxOfTarget(HttpSession apiUser) {
        long targetIdx = SessionUtil.getLoginGuardianIdx(apiUser);
        FamilyIdxResponse getFamilyIdx = guardiansService.getFamilyIdxOfTarget(targetIdx);
        SuccessResponse response = new SuccessResponse(true, "보호자의 모든 가족 Idx 조회 성공", getFamilyIdx);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 보호자 프로필 수정 (일부)
     * @param profileRequest
     * @param apiUser
     * @return
     */
    @PostMapping ("add/more-seniors")
    @LoginCheck(type = UserType.GUARDIAN)
    public ResponseEntity<?> addMoreSeniors(@RequestBody @Valid RegisterSeniorRequest registerSeniorReq, HttpSession apiUser) {
        String loginCode = seniorsService.insertAndSetRelations(SessionUtil.getLoginGuardianIdx(apiUser), registerSeniorReq.toEncryptedUser());
        SuccessResponse response = new SuccessResponse(true, "시니어 추가 등록 성공", loginCode);

        return new ResponseEntity<>(response, HttpStatus.PARTIAL_CONTENT);
    }

    /**
     * 보호자 프로필 수정 (일부)
     * @param profileRequest
     * @param apiUser
     * @return
     */
    @GetMapping("add/other-guardians/{guardianIdx}")
    @LoginCheck(type = UserType.GUARDIAN)
    public ResponseEntity<?> addGuardian(@PathVariable Long guardianIdx, HttpSession apiUser) {
        guardiansService.addGuardian(guardianIdx, SessionUtil.getLoginGuardianIdx(apiUser));
        SuccessResponse response = new SuccessResponse(true, "다른 보호자 추가 등록 성공", null);

        return new ResponseEntity<>(response, HttpStatus.PARTIAL_CONTENT);
    }

    /**
     * 보호자 프로필 수정 (일부)
     * @param profileRequest
     * @param apiUser
     * @return
     */
    @PatchMapping ("profile")
    @LoginCheck(type = UserType.GUARDIAN)
    public ResponseEntity<?> editProfile(@RequestBody @Valid UpdateGuardianProfileRequest profileRequest,
                                           HttpSession apiUser) throws IOException {
        guardiansService.updateProfile(SessionUtil.getLoginGuardianIdx(apiUser), profileRequest.toUser());
        SuccessResponse response = new SuccessResponse(true, "보호자의 프로필 수정 성공", null);

        return new ResponseEntity<>(response, HttpStatus.PARTIAL_CONTENT);
    }

    @PatchMapping ("profile/image")
    @LoginCheck(type = UserType.GUARDIAN)
    public ResponseEntity<?> editProfileImage(@RequestParam(value = "url", required = false) @NotNull final MultipartFile multipartFile,
                                           HttpSession apiUser) throws IOException {
        guardiansService.updateProfileImage(SessionUtil.getLoginGuardianIdx(apiUser), multipartFile);
        SuccessResponse response = new SuccessResponse(true, "보호자의 프로필 이미지 수정 성공", null);

        return new ResponseEntity<>(response, HttpStatus.PARTIAL_CONTENT);
    }

    @PatchMapping ("profile/of-senior/{seniorIdx}")
    @LoginCheck(type = UserType.GUARDIAN)
    public ResponseEntity<?> editSeniorProfile(@RequestBody @Valid UpdateSeniorProfileRequest profileRequest,
                                               @PathVariable Long seniorIdx) throws IOException {
        seniorsService.updateProfile(seniorIdx, profileRequest.toUser());
        SuccessResponse response = new SuccessResponse(true, "보호자의 프로필 수정 성공", null);

        return new ResponseEntity<>(response, HttpStatus.PARTIAL_CONTENT);
    }

    @PatchMapping ("profile/image/of-senior/{seniorIdx}")
    @LoginCheck(type = UserType.GUARDIAN)
    public ResponseEntity<?> editSeniorProfileImage(@RequestParam(value = "url", required = false) @NotNull final MultipartFile multipartFile,
                                                    @PathVariable Long seniorIdx) throws IOException {
        seniorsService.updateProfileImage(seniorIdx, multipartFile);
        SuccessResponse response = new SuccessResponse(true, "보호자의 프로필 이미지 수정 성공", null);

        return new ResponseEntity<>(response, HttpStatus.PARTIAL_CONTENT);
    }
}
