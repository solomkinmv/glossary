package io.github.solomkinmv.glossary.service.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
@Getter
@Setter
public class StorageProperties {
    private String imgUrlPrefix = "/img";
    private String imgUploadDir = "img-upload";
    private String soundUrlPrefix = "/sound";
    private String soundUploadDir = "sound-upload";
}
