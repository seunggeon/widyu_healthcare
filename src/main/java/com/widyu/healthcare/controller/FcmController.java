package com.widyu.healthcare.controller;


import com.google.firebase.database.annotations.NotNull;
import com.widyu.healthcare.dto.FcmDTO;
import com.widyu.healthcare.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FcmController {

    private final FcmService fcmService;

    @PostMapping("/api/fcm")
    public ResponseEntity pushMessage(@RequestBody @NotNull FcmDTO fcmDTO) throws IOException {

        String token = fcmDTO.getMessage().getToken();
        String title = fcmDTO.getMessage().getNotification().getTitle();
        String body = fcmDTO.getMessage().getNotification().getBody();

        fcmService.sendMessageTo(
                token,
                title,
                body);
        return ResponseEntity.ok().build();
    }
}