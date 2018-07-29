package io.github.solomkinmv.glossary.words;

import io.github.solomkinmv.glossary.words.config.SecurityConfiguration.TokenDetails;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableFeignClients
@SpringCloudApplication
@EnableResourceServer
public class WordsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordsServiceApplication.class, args);
    }
}

@RestController
        // todo: remove this
class MainController {

    @GetMapping("/")
    public String index(OAuth2Authentication p) {
        TokenDetails details = (TokenDetails) p.getDetails();
        return "words " + p + " " + details;
    }
}