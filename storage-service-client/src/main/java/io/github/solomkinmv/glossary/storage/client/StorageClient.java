package io.github.solomkinmv.glossary.storage.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@FeignClient(
        name = "storage-service",
        path = "/storage-service"
)
public interface StorageClient {

    String UPLOAD_IMG_KEY = "file";

    @GetMapping(value = "/", consumes = APPLICATION_JSON_VALUE)
    Optional<String> get(@RequestParam("type") StoredType type, @RequestParam("filename") String filename);

    @PostMapping(value = "/", consumes = MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> save(@RequestParam("type") StoredType type,
                              @RequestPart(UPLOAD_IMG_KEY) MultipartFile file);

    @DeleteMapping("/")
    ResponseEntity<Void> delete(@RequestParam("type") StoredType type, @RequestParam(value = "filename") String filename);

    @DeleteMapping("/")
    ResponseEntity<Void> delete(@RequestParam("type") StoredType type);

}
