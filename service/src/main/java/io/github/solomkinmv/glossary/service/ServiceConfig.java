package io.github.solomkinmv.glossary.service;

import io.github.solomkinmv.glossary.persistence.PersistenceConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(PersistenceConfig.class)
public class ServiceConfig {
}
