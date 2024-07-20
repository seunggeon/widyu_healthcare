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

    public void sendMessage(String targetToken, Situation situation, String username, String goalname) throws IOException, MissingTokenException {
        if (targetToken == null)
            throw new MissingTokenException("FCM token 값이 없습니다.");

        String message = makeMessage(targetToken, situation, username, goalname);

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

    public void sendMessageToGuardians(long seniorIdx, Situation situation, String username, String goalname){
        List<GuardianInfoResponse> guardianInfoResponsesList = seniorsMapper.findGuardiansByIdx(seniorIdx);
        guardianInfoResponsesList.forEach(guardianInfoResponse -> {
            long guardianIdx = guardianInfoResponse.getUserIdx();
            try {
                this.sendMessage(seniorsMapper.findFCM(guardianIdx), situation, username, goalname);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // FCM 전송 상황 구분
    public enum Situation {
        HEALTH_ALERT,
        GOAL_FAILURE,
        GOAL_TIME,
        GOAL_COMPLETION,
        GOAL_ALL_COMPLETION
    }

    public String makeMessage(String targetToken, Situation situation, String username, String goalname) throws JsonProcessingException {
        String title;
        String body;

        switch (situation) {
            case HEALTH_ALERT:
                title = username + " 님의 건강 수치가 평소와 달라요!";
                body = "앱에서 확인하고 연락을 해보는 것이 좋을 것 같아요.";
                break;
            case GOAL_FAILURE:
                title = username + " 님이 " + goalname + "을 하지 않았어요.";
                body = "확인하러 가기";
                break;
            case GOAL_TIME:
                title = goalname + "을 수행할 시간이에요.";
                body = "확인하러 가기";
                break;
            case GOAL_COMPLETION:
                title = goalname + "를 수행하셨어요.";
                body = "확인하러 가기";
                break;
            case GOAL_ALL_COMPLETION:
                title = "오늘 목표를 모두 수행하셨어요.";
                body = "내일도 힘내세요!";
                break;
            default:
                throw new IllegalArgumentException("Unknown situation: " + situation);
        }

        Fcm fcmMessage = Fcm.builder()
                .message(Fcm.Message.builder()
                        .token(targetToken)
                        .notification(Fcm.Notification.builder()
                                .title(title)
                                .body(body)
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