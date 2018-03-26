package io.github.solomkinmv.glossary.image.storage;

import io.github.solomkinmv.glossary.image.exception.ImageProcessingException;
import io.github.solomkinmv.glossary.storage.client.StorageClient;
import io.github.solomkinmv.glossary.storage.client.StoredType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Component
public class StorageServiceFacade {
    private final StorageClient storageClient;

    public String save(InputStream is, String filename) {
        log.trace("Saving file to storage service {}", filename);
        ResponseEntity<Void> responseEntity = storageClient.save(StoredType.IMG, createMultipart(is, filename));

        log.debug("Saved image file. Location: {}", responseEntity.getHeaders().getLocation());
        return Objects.requireNonNull(responseEntity.getHeaders().getLocation()).toString();
    }

    public Optional<String> get(String filename) {
        log.trace("Getting image file from storage service: {}", filename);
        Optional<String> optionalUrl = storageClient.get(StoredType.SOUND, filename);

        log.debug("Get result from storage service [filename: {}, result: {}]", filename, optionalUrl);
        return optionalUrl;
    }

    public void deleteObject(String filename) {
        log.trace("Deleting image with filename: {}", filename);

        storageClient.delete(StoredType.IMG, filename);

        log.debug("Successfully deleted image with filename: {}", filename);
    }

    public void deleteStorage() {
        log.trace("Deleting images storage");

        storageClient.delete(StoredType.IMG);

        log.debug("Successfully deleted images storage");
    }

    private MultipartFile createMultipart(InputStream is, String filename) {
        FileItem fileItem = new DiskFileItemFactory().createItem("file", MediaType.IMAGE_PNG_VALUE, true, filename);

        try (OutputStream os = fileItem.getOutputStream()) {
            IOUtils.copy(is, os);
        } catch (IOException e) {
            throw new ImageProcessingException("Failed to convert input stream to multipart file", e);
        }

        return new CommonsMultipartFile(fileItem);
    }
}
