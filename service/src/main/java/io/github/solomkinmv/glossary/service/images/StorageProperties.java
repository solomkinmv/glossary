package io.github.solomkinmv.glossary.service.images;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {
    private String imgPrefix = "/img";
    private String imgUploadDir = "img-upload";

    public String getImgUploadDir() {
        return imgUploadDir;
    }

    public void setImgUploadDir(String imgUploadDir) {
        this.imgUploadDir = imgUploadDir;
    }

    public String getImgPrefix() {
        return imgPrefix;
    }

    public void setImgPrefix(String imgPrefix) {
        this.imgPrefix = imgPrefix;
    }
}
