package com.widyu.healthcare.core.api.controller.v1;

import com.widyu.healthcare.core.api.controller.v1.request.health.AppendSeniorHeartBitRequest;
import com.widyu.healthcare.core.api.controller.v1.request.health.UpdateSeniorLocationRequest;
import com.widyu.healthcare.core.api.controller.v1.response.SuccessResponse;
import com.widyu.healthcare.core.api.controller.v1.response.health.GuardianMainHealthResponse;
import com.widyu.healthcare.core.api.controller.v1.response.health.HealthTypeResponse;
import com.widyu.healthcare.core.api.controller.v1.response.health.SeniorDetailHealthResponse;
import com.widyu.healthcare.core.api.controller.v1.response.health.SeniorMainHealthResponse;
import com.widyu.healthcare.core.api.middleware.LoginCheck;
import com.widyu.healthcare.core.domain.domain.v1.HealthType;
import com.widyu.healthcare.core.domain.service.v1.HealthsService;
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
@RequestMapping("/api/v1/health")
public class HealthsController {
    private final HealthsService healthsService;

    @PostMapping("append/heart-bit")
    public ResponseEntity<?> appendHeartBitFromWatch(@RequestBody @Valid AppendSeniorHeartBitRequest heartBitReq, HttpSession apiUser) {
        healthsService.insertRecentHeartBitAndStatus(SessionUtil.getLoginSeniorIdx(apiUser), heartBitReq.toHealthData());
        SuccessResponse response = new SuccessResponse(true, "심장 박동수 DB 저장", null);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PatchMapping("update/location")
    public ResponseEntity<?> updateLocation(@RequestBody @Valid UpdateSeniorLocationRequest locationReq, HttpSession apiUser) {
        healthsService.updateRecentLocation(SessionUtil.getLoginSeniorIdx(apiUser), locationReq.toLocation());
        SuccessResponse response = new SuccessResponse(true, "최신 위치 데이터 DB 저장", null);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("info/of-senior/main")
    @LoginCheck(type = LoginCheck.UserType.SENIOR)
    public ResponseEntity<?> SeniorMainHealthPage(HttpSession apiUser) {
        SeniorMainHealthResponse mainHealthResponse = healthsService.getRecentHealthOfSenior(SessionUtil.getLoginSeniorIdx(apiUser));
        SuccessResponse response = new SuccessResponse(true, "건강 메인 페이지 조회 성공", mainHealthResponse);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("info/of-seniors/main")
    @LoginCheck(type = LoginCheck.UserType.GUARDIAN)
    public ResponseEntity<?> GuardianMainHealthPage(HttpSession apiUser) {
        GuardianMainHealthResponse mainHealthResponse = healthsService.getRecentHealthOfSeniors(SessionUtil.getLoginGuardianIdx(apiUser));
        SuccessResponse response = new SuccessResponse(true, "건강 메인 페이지 조회 성공", mainHealthResponse);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("info/of-senior/detail")
    @LoginCheck(type = LoginCheck.UserType.SENIOR)
    public ResponseEntity<?> DetailHealthPage(HttpSession apiUser) {
        SeniorDetailHealthResponse detailHealthResponse = healthsService.getSeniorDetailInfoAndHealthInfo(SessionUtil.getLoginSeniorIdx(apiUser));
        SuccessResponse response = new SuccessResponse(true, "건강 상세 페이지 조회 성공", detailHealthResponse);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("info/of-senior/{type}")
    @LoginCheck(type = LoginCheck.UserType.COMMON)
    public ResponseEntity<?> findHeartBitInfo(@PathVariable String type, HttpSession apiUser) {
        HealthTypeResponse heartBitResponse = healthsService.getDailyHearth(SessionUtil.getLoginSeniorIdx(apiUser), HealthType.valueOf(type));
        SuccessResponse response = new SuccessResponse(true, "심장 박동 수 조회 성공", heartBitResponse);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
