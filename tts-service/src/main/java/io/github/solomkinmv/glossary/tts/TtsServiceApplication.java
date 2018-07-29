package io.github.solomkinmv.glossary.tts;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableFeignClients
@SpringCloudApplication
@EnableResourceServer
public class TtsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TtsServiceApplication.class, args);
    }
}
