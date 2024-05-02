package com.widyu.healthcare.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.widyu.healthcare.dto.domain.FcmDto;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FcmService {

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/widyu-1fb84/messages:send";
    private final ObjectMapper objectMapper;

    public void sendMessage(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = okhttp3.RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post((okhttp3.RequestBody) requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println("response = " + response);
        if (!response.isSuccessful()) {
            throw new IOException("FCM request failed with code: " + response.code() + ", message: " + response.message());

        }
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonParseException, JsonProcessingException {

        FcmDto fcmMessage = FcmDto.builder()
                .message(FcmDto.Message.builder()
                        .token(targetToken)
                        .notification(FcmDto.Notification.builder()
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