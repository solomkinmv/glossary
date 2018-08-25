package io.github.solomkinmv.glossary.tts.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClient;
import io.github.solomkinmv.glossary.tts.service.SpeechService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
public class PollyConfiguration {

    @Bean
    AmazonPolly amazonPolly() {
        return AmazonPollyClient.builder()
                                .withCredentials(new DefaultAWSCredentialsProviderChain())
                                .withRegion(Regions.US_EAST_1)
                                .withClientConfiguration(new ClientConfiguration())  // todo: try without this
                                .build();
    }

    @Bean
    @Primary
    @Profile("stub")
    SpeechService stubSpeechService() {
        return speech -> "http://localhost:8081/sound/bells.mp3";
    }

}
