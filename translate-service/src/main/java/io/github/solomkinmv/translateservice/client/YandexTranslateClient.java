package io.github.solomkinmv.translateservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(url = "https://translate.yandex.net", name = "yandex-translate")
public interface YandexTranslateClient {

    @PostMapping(value = "/api/v1.5/tr.json/translate", consumes = APPLICATION_JSON_VALUE)
    YandexTranslationResult translate(@RequestParam("key") String key,
                                      @RequestParam("text") String text,
                                      @RequestParam("lang") String translationDirection);
}
