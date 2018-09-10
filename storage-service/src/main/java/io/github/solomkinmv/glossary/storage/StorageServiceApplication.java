package io.github.solomkinmv.glossary.storage;

import io.github.solomkinmv.glossary.storage.properties.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringCloudApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(StorageProperties.class)
public class StorageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StorageServiceApplication.class, args);
    }
}
