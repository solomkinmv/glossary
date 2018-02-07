package io.github.solomkinmv.glossary.storage.controller;

import io.github.solomkinmv.glossary.storage.exception.UploadException;
import io.github.solomkinmv.glossary.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StorageController {

    private static final String UPLOAD_IMG_KEY = "file";

    private final StorageService storageService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Void> save(@RequestParam(UPLOAD_IMG_KEY) MultipartFile file) {
        log.info("Uploading file with name: {}", file.getOriginalFilename());
        if (file.isEmpty()) {
            String message = "Failed to store empty file: " + file.getOriginalFilename();
            log.error(message);
            throw new UploadException(message);
        }
        String uriLocation;
        try {
            uriLocation = storageService.store(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            String message = "Failed to store empty file: " + file.getOriginalFilename();
            log.error(message);
            throw new UploadException(message, e);
        }
        return ResponseEntity.created(URI.create(uriLocation)).build();
    }
}
