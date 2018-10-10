package io.github.solomkinmv.glossary.words;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableFeignClients
@SpringCloudApplication
@EnableResourceServer
@EnableBinding(Source.class)
public class WordsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordsServiceApplication.class, args);
    }
}