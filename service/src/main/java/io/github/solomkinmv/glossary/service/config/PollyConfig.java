package io.github.solomkinmv.glossary.service.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PollyConfig {
    @Bean
    AmazonPolly amazonPolly() {
        return AmazonPollyClient.builder()
                                .withCredentials(new DefaultAWSCredentialsProviderChain())
                                .withRegion(Regions.US_EAST_1)
                                .withClientConfiguration(new ClientConfiguration())  // todo: try without this
                                .build();
    }
}
