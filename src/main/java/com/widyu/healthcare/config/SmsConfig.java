package com.widyu.healthcare.config;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfig {

    @Value("${cool.sms.apiKey}")
    private String apiKey;

    @Value("${cool.sms.apiSecretKey}")
    private String apiSecretKey;

    @Value("${cool.sms.domain}")
    private String domain;

    @Bean
    public DefaultMessageService messageService() {
        return  NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, domain);
    }
}