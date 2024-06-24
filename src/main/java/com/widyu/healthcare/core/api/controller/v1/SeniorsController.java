package com.widyu.healthcare.core.api.controller.v1;

import com.google.firebase.database.annotations.NotNull;
import com.widyu.healthcare.core.api.controller.v1.request.senior.AppendDiseaseRequest;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/senior")
public class SeniorsController {
    private final SeniorsService seniorsService;

    /**
     * @version : 1.0.0
     * @author : seunggeon
     * @breif : 시니어 회원가입
     * @see : Invite Code는 Entity 변환 시 설정, userdetail insert 시 user 생성되는 trigger 설정
     * @param : 보호자 INDEX, 보호자 기본 정보
     * @return : GuardianInfoResponse
     */
    @PostMapping("register/{guardianIdx}")
    public ResponseEntity<?> register(@PathVariable long guardianIdx, @RequestBody @Valid RegisterSeniorRequest seniorInfo) {
        String loginCode = seniorsService.insertAndSetRelations(guardianIdx, seniorInfo.toEncryptedUser());
        SuccessResponse response = new SuccessResponse(true, "시니어 회원가입 성공", loginCode);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    /**
     * @version : 1.0.0
     * @author : seunggeon
     * @breif : 시니어 로그인
     * @param : Invite Code, FCM Token
     * @return : 시니어 INDEX 및 기본 정보
     */
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginSeniorRequest loginReq, HttpSession apiUser) {
        CommonUserResponse seniorInfo = seniorsService.loginByInviteCode(loginReq.getInviteCode(), loginReq.getFcmToken(), apiUser);
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
        SuccessResponse response = new SuccessResponse(true, "유저(시니어) 및 유저의 보호자 정보 조회 성공", guardiansAndTargetInfo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("find/guardians/{guardianId}")
    @LoginCheck(type = LoginCheck.UserType.COMMON)
    public ResponseEntity<?> getGuardianById(@PathVariable String guardianId) {
        GuardianInfoResponse guardianInfo = seniorsService.findGuardianByGuardianId(guardianId);
        SuccessResponse response = new SuccessResponse(true, "찾고자 하는 보호자 ID 조회 성공", guardianInfo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PatchMapping("profile")
    @LoginCheck(type = LoginCheck.UserType.SENIOR)
    public ResponseEntity<?> editProfile(@RequestBody @Valid UpdateSeniorProfileRequest profileReq,
                                         HttpSession apiUser) throws IOException {
        seniorsService.updateProfile(SessionUtil.getLoginSeniorIdx(apiUser), profileReq.toUser());
        SuccessResponse response = new SuccessResponse(true, "시니어 프로필 수정 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PatchMapping("profile/image")
    @LoginCheck(type = LoginCheck.UserType.SENIOR)
    public ResponseEntity<?> editProfileImage(@RequestParam(value = "url", required = false) @NotNull final MultipartFile multipartFile, HttpSession apiUser) throws IOException {
        seniorsService.updateProfileImage(SessionUtil.getLoginSeniorIdx(apiUser), multipartFile);
        SuccessResponse response = new SuccessResponse(true, "시니어 프로필 이미지 수정 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("disease/of-senior/{seniorIdx}")
    @LoginCheck(type = LoginCheck.UserType.COMMON)
    public ResponseEntity<?> addDisease(@RequestBody @Valid AppendDiseaseRequest diseaseReq,
                                         @PathVariable Long seniorIdx) throws IOException {
        seniorsService.insertDisease(seniorIdx, diseaseReq.toDisease());
        SuccessResponse response = new SuccessResponse(true, "질병 추가 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("disease/{diseaseIdx}/of-senior/{seniorIdx}")
    @LoginCheck(type = LoginCheck.UserType.COMMON)
    public ResponseEntity<?> deleteDisease(@PathVariable Long diseaseIdx,
                                            @PathVariable Long seniorIdx) throws IOException {
        seniorsService.deleteDisease(seniorIdx, diseaseIdx);
        SuccessResponse response = new SuccessResponse(true, "질병 삭제 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
