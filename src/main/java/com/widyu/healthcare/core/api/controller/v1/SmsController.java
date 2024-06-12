package com.widyu.healthcare.core.api.controller.v1;

import com.widyu.healthcare.core.api.controller.v1.request.sms.SendSmsRequest;
import com.widyu.healthcare.core.api.controller.v1.request.sms.VerifyRequest;
import com.widyu.healthcare.core.api.controller.v1.response.SuccessResponse;
import com.widyu.healthcare.core.domain.service.v1.SmsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sms")
public class SmsController {
    private final SmsService smsService;

    /**
     * 문자 인증 번호 전송
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendSMS(@RequestBody @Valid SendSmsRequest sendSmsRequest) {
        String certificationCode = smsService.generateCerNum();
        smsService.sendCodeAndSaveRedis(sendSmsRequest.getPhoneNumber(), certificationCode);
        SuccessResponse response = new SuccessResponse(true, "문자 인증번호 발신 성공", certificationCode);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    /**
     * 문자 인증 번호 유효성 확인
     */
    @PostMapping("/verify/{phoneNumber}")
    public  ResponseEntity<?> verify(@PathVariable String phoneNumber, @RequestBody VerifyRequest verifyRequest){
        String certificationCode = verifyRequest.getCertificationCode();
        String message = smsService.verifyCode(phoneNumber, certificationCode);
        SuccessResponse response = new SuccessResponse(true, message, null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}