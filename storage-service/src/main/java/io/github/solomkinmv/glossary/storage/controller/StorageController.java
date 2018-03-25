package io.github.solomkinmv.glossary.storage.controller;

import io.github.solomkinmv.glossary.storage.exception.UploadException;
import io.github.solomkinmv.glossary.storage.service.StorageService;
import io.github.solomkinmv.glossary.storage.service.StoredType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StorageController {

    private static final String UPLOAD_IMG_KEY = "file";

    private final StorageService storageService;

    @GetMapping("/")
    public ResponseEntity<String> get(@RequestParam("type") StoredType type, @RequestParam("filename") String filename) {
        log.info("Getting file [name: {}, type: {}]", filename, type);
        Optional<String> path = storageService.getObject(filename, type);

        return path.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/")
    public ResponseEntity<Void> save(@RequestParam("type") StoredType type,
                                     @RequestParam(UPLOAD_IMG_KEY) MultipartFile file) {
        log.info("Uploading file with [name: {}, type: {}, contentType: {}]",
                 file.getOriginalFilename(), type, file.getContentType());
        if (file.isEmpty()) {
            String message = "Failed to store empty file: " + file.getOriginalFilename();
            log.error(message);
            throw new UploadException(message);
        }
        String uriLocation;
        try {
            uriLocation = storageService.store(file.getInputStream(), file.getOriginalFilename(), type);
        } catch (IOException e) {
            String message = "Failed to store empty file: " + file.getOriginalFilename();
            log.error(message);
            throw new UploadException(message, e);
        }
        return ResponseEntity.created(URI.create(uriLocation))
                             .build();
    }

    @DeleteMapping("/")
    public void delete(@RequestParam("type") StoredType type,
                       @RequestParam(value = "filename", required = false) Optional<String> filename) {
        log.info("Deleting file [name: {}, type: {}]", filename.toString(), type);

        if (filename.isPresent()) {
            storageService.deleteObject(filename.get(), type);
        } else {
            storageService.deleteStorageByType(type);
        }
    }
}
