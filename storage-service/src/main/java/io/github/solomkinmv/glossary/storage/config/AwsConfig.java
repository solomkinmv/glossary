package io.github.solomkinmv.glossary.storage.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    @Bean
    AmazonPolly amazonPolly() {
        return AmazonPollyClient.builder()
                                .withCredentials(new DefaultAWSCredentialsProviderChain())
                                .withRegion(Regions.US_EAST_1)
                                .withClientConfiguration(new ClientConfiguration())  // todo: try without this
                                .build();
    }

    @Bean
    AmazonS3 amazonS3() {
        return AmazonS3Client.builder()
                             .withCredentials(new DefaultAWSCredentialsProviderChain())
                             .withRegion(Regions.US_WEST_2)
                             .build();
    }
}
