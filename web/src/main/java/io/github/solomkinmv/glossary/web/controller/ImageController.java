package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.service.images.ImageService;
import io.github.solomkinmv.glossary.web.dto.ImageDto;
import io.github.solomkinmv.glossary.web.exception.UploadException;
import io.github.solomkinmv.glossary.web.resource.ImageSearchResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ImageSearchResource search(@RequestParam("query") String query) {
        log.info("Searching for images by following query: {}", query);
        return new ImageSearchResource(query, imageService.search(query.split(",")));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Void> uploadImage(@RequestParam(UPLOAD_IMG_KEY) MultipartFile file) {
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

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUploadedImage(@Validated @RequestBody ImageDto imageDto) {
        String imgFilename = imageDto.getImage();
        log.info("Deleting image by filename {}", imgFilename);
        imageService.deleteImg(imgFilename);
        return ResponseEntity.ok().build();
    }
}
