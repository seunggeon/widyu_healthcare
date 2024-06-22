package com.widyu.healthcare.core.api.controller.v1;

import com.google.firebase.database.annotations.NotNull;
import com.widyu.healthcare.core.domain.domain.v1.Fcm;
import com.widyu.healthcare.core.api.controller.v1.response.SuccessResponse;
import com.widyu.healthcare.core.domain.service.v1.FcmService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fcm")
public class FcmController {
    private final FcmService fcmService;
    @PostMapping("/send")
    public ResponseEntity<?> pushMessage(@RequestBody @NotNull Fcm fcmDTO) throws IOException {

        if (Fcm.hasNullDataBeforeFcmSend(fcmDTO)){
            log.error("send Fcm Error! {}", fcmDTO);
            throw  new NullPointerException("Fcm 전송 시 필수 데이터를 모두 입력해야 합니다.");
        }

        String token = fcmDTO.getMessage().getToken();
        String title = fcmDTO.getMessage().getNotification().getTitle();
        String body = fcmDTO.getMessage().getNotification().getBody();

        fcmService.sendMessage(token, title, body);
        SuccessResponse response = new SuccessResponse(true, "fcm send 성공", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}