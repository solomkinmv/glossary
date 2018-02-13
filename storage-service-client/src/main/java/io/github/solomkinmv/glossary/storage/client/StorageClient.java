package io.github.solomkinmv.glossary.storage.client;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(
        name = "storage-service",
        path = "/storage-service",
        configuration = StorageClient.MultipartSupportConfig.class
)
public interface StorageClient {

    String UPLOAD_IMG_KEY = "file";

    @GetMapping("/")
    ResponseEntity<String> get(@RequestParam("type") StoredType type, @RequestParam("filename") String filename);

    @PostMapping("/")
    ResponseEntity<Void> save(@RequestParam("type") StoredType type,
                              @RequestPart(UPLOAD_IMG_KEY) MultipartFile file);

    @DeleteMapping("/")
    ResponseEntity<Void> delete(@RequestParam("type") StoredType type, @RequestParam(value = "filename") String filename);

    @DeleteMapping("/")
    ResponseEntity<Void> delete(@RequestParam("type") StoredType type);

    class MultipartSupportConfig {

        @Autowired
        private ObjectFactory<HttpMessageConverters> messageConverters;

        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder(new SpringEncoder(messageConverters));
        }
    }
}
