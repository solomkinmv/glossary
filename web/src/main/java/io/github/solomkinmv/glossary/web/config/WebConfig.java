package io.github.solomkinmv.glossary.web.config;

import io.github.solomkinmv.glossary.service.storage.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.nio.file.Paths;

/**
 * Spring web configuration.
 * Configures static resource handler for the uploaded images.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    private static final int CACHE_PERIOD = 3600;
    private static final String RESOURCE_HANDLER_FORMAT = "/%s/**";
    private final StorageProperties storageProperties;

    @Autowired
    public WebConfig(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String externalImgDir = Paths.get(storageProperties.getImgUploadDir()).toUri().toString();
        String imgResourceHandlerPath = String.format(RESOURCE_HANDLER_FORMAT, storageProperties.getImgUrlPrefix());
        addResourceHandler(registry, externalImgDir, imgResourceHandlerPath);

        String externalSoundDir = Paths.get(storageProperties.getSoundUploadDir()).toUri().toString();
        String soundResourceHandlerPath = String.format(RESOURCE_HANDLER_FORMAT, storageProperties.getSoundUrlPrefix());
        addResourceHandler(registry, externalSoundDir, soundResourceHandlerPath);
    }

    private void addResourceHandler(ResourceHandlerRegistry registry, String externalResourceDir, String resourceHandlerPath) {
        registry
                .addResourceHandler(resourceHandlerPath)
                .addResourceLocations(externalResourceDir)
                .setCachePeriod(CACHE_PERIOD)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}
