package io.github.solomkinmv.glossary.tts.storage;

import io.github.solomkinmv.glossary.storage.client.StorageClient;
import io.github.solomkinmv.glossary.storage.client.StoredType;
import io.github.solomkinmv.glossary.tts.exception.TtsServiceException;
import lombok.AllArgsConstructor;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Component
public class StorageServiceFacade {
    private final StorageClient storageClient;

    public String save(InputStream is, String filename) {
        ResponseEntity<Void> responseEntity = storageClient.save(StoredType.SOUND, createMultipart(is, filename));

        return Objects.requireNonNull(responseEntity.getHeaders().getLocation()).getPath();
    }

    public Optional<String> get(String filename) {
        return storageClient.get(StoredType.SOUND, filename);
    }

    private MultipartFile createMultipart(InputStream is, String filename) {
        FileItem fileItem = new DiskFileItemFactory().createItem("file", "audio/mpeg", true, filename);

        try (OutputStream os = fileItem.getOutputStream()) {
            IOUtils.copy(is, os);
        } catch (IOException e) {
            throw new TtsServiceException("Failed to convert input stream to multipart file", e);
        }

        return new CommonsMultipartFile(fileItem);
    }
}
