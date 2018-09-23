package io.github.solomkinmv.glossary.translateserviceclient;

import io.github.solomkinmv.glossary.translateserviceclient.domain.Language;
import io.github.solomkinmv.glossary.translateserviceclient.domain.TranslateResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(
        name = "translate-service",
        fallbackFactory = TranslateClientFallbackFactory.class
)
public interface TranslateClient {

    @PostMapping(value = "/translate", consumes = APPLICATION_JSON_VALUE)
    TranslateResult translate(@RequestParam("text") String text,
                              @RequestParam(value = "source") Language source,
                              @RequestParam("target") Language translationDirection);

    @PostMapping(value = "/translate", consumes = APPLICATION_JSON_VALUE)
    TranslateResult translate(@RequestParam("text") String text,
                              @RequestParam("target") Language translationDirection);
}