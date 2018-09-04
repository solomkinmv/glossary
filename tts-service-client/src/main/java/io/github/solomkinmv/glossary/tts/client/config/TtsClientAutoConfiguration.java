package io.github.solomkinmv.glossary.tts.client.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "tts-client.library", name = "enabled",
        havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(FeignRibbonClientAutoConfiguration.class)
@EnableFeignClients(basePackages = "io.github.solomkinmv.glossary.tts.client")
public class TtsClientAutoConfiguration {

}
