package io.github.solomkinmv.glossary.storage.config;

import io.github.solomkinmv.glossary.storage.properties.StorageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.nio.file.Paths;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ResourceConfig implements WebMvcConfigurer {
    private static final int CACHE_PERIOD = 3600;
    private static final String RESOURCE_HANDLER_FORMAT = "%s/**";
    private static final String RESOURCE_LOCATION_FORMAT = "file:%s/";
    private final StorageProperties storageProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // FIXME: check file location
        String externalImgDir = String.format(RESOURCE_LOCATION_FORMAT,
                                              Paths.get(storageProperties.getImgUploadDir())
                                                   .toAbsolutePath());
        String imgResourceHandlerPath = String.format(RESOURCE_HANDLER_FORMAT, storageProperties.getImgUrlPrefix());
        addResourceHandler(registry, externalImgDir, imgResourceHandlerPath);

        String externalSoundDir = String.format(RESOURCE_LOCATION_FORMAT,
                                                Paths.get(storageProperties.getSoundUploadDir())
                                                     .toAbsolutePath());
        String soundResourceHandlerPath = String.format(RESOURCE_HANDLER_FORMAT, storageProperties.getSoundUrlPrefix());
        addResourceHandler(registry, externalSoundDir, soundResourceHandlerPath);
    }

    private void addResourceHandler(ResourceHandlerRegistry registry, String externalResourceDir, String resourceHandlerPath) {
        log.info("Adding resource handler. External resource dir: {}. Resource handler path: {}",
                 externalResourceDir, resourceHandlerPath);
        registry
                .addResourceHandler(resourceHandlerPath)
                .addResourceLocations(externalResourceDir)
                .setCachePeriod(CACHE_PERIOD)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}
