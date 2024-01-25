package com.widyu.healthcare.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FcmConfig {

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException{

        FileInputStream serviceAccount =
                new FileInputStream("src/main/resources/widyu-1fb84-firebase-adminsdk-c8txe-0a0cf6bd01.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);

        return FirebaseMessaging.getInstance(firebaseApp);
    }


}
