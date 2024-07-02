package com.widyu.healthcare.core.domain.service.v1;

import com.widyu.healthcare.core.api.controller.v1.response.guardian.GuardianInfoResponse;
import com.widyu.healthcare.core.db.mapper.v1.SeniorsMapper;
import com.widyu.healthcare.core.domain.domain.v1.Fcm;
import com.widyu.healthcare.core.domain.domain.v1.GoalType;
import com.widyu.healthcare.support.error.exception.MissingTokenException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Log4j2
@Service
@RequiredArgsConstructor
public class FcmService {

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/widyu-1fb84/messages:send";
    private final ObjectMapper objectMapper;
    private final SeniorsMapper seniorsMapper;

    public void sendMessage(String targetToken, String title, String type) throws IOException, MissingTokenException {
        if (targetToken == null)
            throw new MissingTokenException("FCM token 값이 없습니다.");

        String message = makeMessage(targetToken, title, new FcmBody(type));

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = okhttp3.RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("response = " + response);
            if (!response.isSuccessful()) {
                throw new IOException("FCM request failed with code: " + response.code() + ", message: " + response.message());
            }
        }
    }

    public void sendMessageToGuardians(long seniorIdx, String title, String type){
        List<GuardianInfoResponse> guardianInfoResponsesList = seniorsMapper.findGuardiansByIdx(seniorIdx);
        guardianInfoResponsesList.forEach(guardianInfoResponse -> {
            long guardianIdx = guardianInfoResponse.getUserIdx();
            try {
                this.sendMessage(seniorsMapper.findFCM(guardianIdx), title, type);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private class FcmBody {
        String type;

        public FcmBody(String type) {
            this.type = type;
        }
    }

    private String makeMessage(String targetToken, String title, FcmBody body) throws JsonParseException, JsonProcessingException {

        Fcm fcmMessage = Fcm.builder()
                .message(Fcm.Message.builder()
                        .token(targetToken)
                        .notification(Fcm.Notification.builder()
                                .title(title)
                                .body(body.toString())
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "widyu-1fb84-firebase-adminsdk-c8txe-0a0cf6bd01.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}