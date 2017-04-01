package io.github.solomkinmv.glossary.service.bootstrap;

import io.github.solomkinmv.glossary.service.storage.StorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Bootstraps data for the service layer.
 */
@Component
@Profile("dev")
@Slf4j
public class SpringResourceHandlerBootstrap implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private final StorageProperties storageProperties;

    @Autowired
    public SpringResourceHandlerBootstrap(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        removeUploadDir();
    }

    private void removeUploadDir() {
        Path imgUploadPath = Paths.get(storageProperties.getImgUploadDir());
        if (Files.exists(imgUploadPath)) {
            FileSystemUtils.deleteRecursively(imgUploadPath.toFile());
        }
    }
}
