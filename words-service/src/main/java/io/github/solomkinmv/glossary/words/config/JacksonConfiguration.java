package io.github.solomkinmv.glossary.words.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Bean
    public ParameterNamesModule parameterNamesModule() {
        return new ParameterNamesModule(JsonCreator.Mode.PROPERTIES);
    }
}
