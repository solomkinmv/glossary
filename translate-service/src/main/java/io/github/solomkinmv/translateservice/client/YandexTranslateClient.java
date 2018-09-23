package io.github.solomkinmv.translateservice.client;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static java.util.Collections.emptyList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(url = "https://translate.yandex.net", name = "yandex-translate", fallbackFactory = YandexTranslateClientFallbackFactory.class)
public interface YandexTranslateClient {

    @PostMapping(value = "/api/v1.5/tr.json/translate",
            consumes = APPLICATION_JSON_VALUE)
    YandexTranslationResult translate(@RequestParam("key") String key,
                                      @RequestParam("text") String text,
                                      @RequestParam("lang") String translationDirection);
}

@Slf4j
@Component
class YandexTranslateClientFallbackFactory implements FallbackFactory<YandexTranslateClient> {

    @Override
    public YandexTranslateClient create(Throwable cause) {
        return (key, text, translationDirection) -> {
            log.warn("Failed to get translation result [key: {}, text: {}, translationDirection: {}]",
                     key, text, translationDirection, cause);
            return new YandexTranslationResult((short) 200, translationDirection, emptyList());
        };
    }
}