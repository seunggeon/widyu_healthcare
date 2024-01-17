package com.widyu.healthcare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync // 메서드를 비동기 방식으로 실행할 수 있도록 설정한다.
@EnableScheduling // 스케줄링을 허용한다.
//@EnableCaching // 캐싱을 허용한다.
public class WidyuHealthcareApplication {

    public static void main(String[] args) {
        SpringApplication.run(WidyuHealthcareApplication.class, args);
    }

}
