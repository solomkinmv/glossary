package io.github.solomkinmv.glossary.image.controller;

import io.github.solomkinmv.glossary.image.exception.ImageProcessingException;
import io.github.solomkinmv.glossary.image.service.ImageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
public class ImageController {
    private static final String UPLOAD_IMG_KEY = "file";

    private final ImageService imageService;

    @GetMapping("/search")
    public List<String> search(@RequestParam("tags") String[] tags) {
        return imageService.search(tags);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Void> uploadImage(@RequestParam(UPLOAD_IMG_KEY) MultipartFile file) {
        log.info("Uploading file with name: {}", file.getOriginalFilename());
        if (file.isEmpty()) {
            String message = "Failed to store empty file: {}" + file.getOriginalFilename();
            log.error(message);
            throw new ImageProcessingException(message);
        }
        String uriLocation;
        try {
            uriLocation = imageService.store(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            String message = "Failed to store empty file: " + file.getOriginalFilename();
            log.error(message);
            throw new ImageProcessingException(message, e);
        }
        return ResponseEntity.created(URI.create(uriLocation)).build();
    }

    @DeleteMapping("/")
    public void delete(@RequestParam(value = "filename") String filename) {
        log.info("Deleting file [name: {}]", filename);

        imageService.deleteImg(filename);
    }
}

