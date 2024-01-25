package com.widyu.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@AllArgsConstructor
@Getter
public class FcmDTO {
    private boolean validateOnly;
    private Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        @NonNull
        private Notification notification;
        @NonNull
        private String token;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        @NonNull
        private String title;
        private String body;
        private String image;
    }
}
