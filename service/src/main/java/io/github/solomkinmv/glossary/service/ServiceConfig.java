package io.github.solomkinmv.glossary.service;

import io.github.solomkinmv.glossary.persistence.PersistenceConfig;
import io.github.solomkinmv.glossary.service.images.StorageProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@Import(PersistenceConfig.class)
public class ServiceConfig {
}
