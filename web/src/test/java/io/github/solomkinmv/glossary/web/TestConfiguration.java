package io.github.solomkinmv.glossary.web;

import io.github.solomkinmv.glossary.service.translate.Translator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class TestConfiguration {
    @Bean
    public Translator translator() {
        return (text, source, target) -> "illegalWord".equals(text) ?
                Optional.empty() :
                Optional.of(text + "_translation");
    }
}
