package io.github.solomkinmv.glossary.storage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;

@Configuration
public class WebConfig {

    @Bean
    RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
}
