package com.widyu.healthcare.controller;

import com.widyu.healthcare.service.SmsService;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Log4j2
@RestController
@RequestMapping("/sms")
public class SmsController {
    @Autowired
    private SmsService smsService;
    @PostMapping("/send")
    public String sendSMS(@RequestBody SmsRequest smsRequest) {
        String phoneNumber = smsRequest.getPhoneNumber();

        Random rand = new Random();
        String numStr = "";
        for (int i = 0; i < 4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
        }

        System.out.println("수신자 번호 : " + phoneNumber);
        System.out.println("인증번호 : " + numStr);
        smsService.sendOne(phoneNumber, numStr);
        return numStr;
    }

    @Setter
    @Getter
    private static class SmsRequest {
        @NonNull
        private String phoneNumber;
    }
}