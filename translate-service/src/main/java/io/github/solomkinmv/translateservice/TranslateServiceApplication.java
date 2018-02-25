package io.github.solomkinmv.translateservice;

import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableCaching
@SpringCloudApplication
public class TranslateServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TranslateServiceApplication.class, args);
    }
}
