package io.github.solomkinmv.glossary.storage.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    @Bean
    AmazonS3 amazonS3() {
        return AmazonS3Client.builder()
                             .withCredentials(new DefaultAWSCredentialsProviderChain())
                             .withRegion(Regions.US_WEST_2)
                             .build();
    }
}
