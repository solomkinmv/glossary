package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.service.images.ImageService;
import io.github.solomkinmv.glossary.web.dto.ImageDto;
import io.github.solomkinmv.glossary.web.exception.UploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

/**
 * Endpoint for operations with images.
 */
@RestController
@RequestMapping("/api/images")
@Slf4j
public class ImageController {
    private static final String UPLOAD_IMG_KEY = "file";
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // TODO: replace String with HATEAOS resource
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Resources<String> search(@RequestParam("query") String query) {
        log.info("Searching for images by following query: {}", query);
        return new Resources<>(imageService.search(query.split(",")));
    }

    @PostMapping("")
    public ResponseEntity<?> uploadImage(@RequestParam(UPLOAD_IMG_KEY) MultipartFile file) {
        log.info("Uploading file with name: {}", file.getOriginalFilename());
        if (file.isEmpty()) {
            String message = "Failed to store empty file: {}" + file.getOriginalFilename();
            log.error(message);
            throw new UploadException(message);
        }
        String uriLocation;
        try {
            uriLocation = imageService.store(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            String message = "Failed to store empty file: " + file.getOriginalFilename();
            log.error(message);
            throw new UploadException(message, e);
        }
        String uriString = ServletUriComponentsBuilder.fromCurrentContextPath().path(uriLocation).toUriString();
        return ResponseEntity.created(URI.create(uriString)).build();
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteUploadedImage(@RequestBody ImageDto imageDto) {
        String imgFilename = imageDto.getImage();
        log.info("Deleting image by filename {}", imgFilename);
        imageService.deleteImg(imgFilename);
        return ResponseEntity.ok().build();
    }
}
