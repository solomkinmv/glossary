package io.github.solomkinmv.glossary.tts;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringCloudApplication
public class TtsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TtsServiceApplication.class, args);
    }
}
