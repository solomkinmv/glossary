package io.github.solomkinmv.glossary.words;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringCloudApplication
public class WordsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordsServiceApplication.class, args);
    }
}
