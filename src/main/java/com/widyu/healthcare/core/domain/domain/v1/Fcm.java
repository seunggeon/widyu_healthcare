package com.widyu.healthcare.core.domain.domain.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@AllArgsConstructor
@Getter
public class Fcm {
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

    public static boolean hasNullDataBeforeFcmSend(Fcm fcmDTO){
        return fcmDTO.getMessage() == null || fcmDTO.getMessage().getToken() == null ||
                fcmDTO.getMessage().getNotification() == null || fcmDTO.getMessage().getNotification().getTitle() == null;
    }
}
