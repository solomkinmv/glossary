package io.github.solomkinmv.glossary.storage.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("storage")
public class StorageProperties {
    private String imgUrlPrefix = "/img";
    private String imgUploadDir = "img-upload";
    private String soundUrlPrefix = "/sound";
    private String soundUploadDir = "sound-upload";
    private String imageS3BucketName = "glossary-images";
    private String pronunciationS3BucketName = "glossary-pronunciations";
}
