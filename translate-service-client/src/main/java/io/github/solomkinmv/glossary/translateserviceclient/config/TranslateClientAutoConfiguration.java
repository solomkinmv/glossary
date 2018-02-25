package io.github.solomkinmv.glossary.translateserviceclient.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "storage-client.library", name = "enabled",
        havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(FeignRibbonClientAutoConfiguration.class)
@EnableFeignClients(basePackages = "io.github.solomkinmv.glossary.translateserviceclient")
public class TranslateClientAutoConfiguration {
}
