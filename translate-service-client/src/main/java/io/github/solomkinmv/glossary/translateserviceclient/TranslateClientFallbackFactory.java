package io.github.solomkinmv.glossary.translateserviceclient;

import feign.hystrix.FallbackFactory;
import io.github.solomkinmv.glossary.translateserviceclient.domain.Language;
import io.github.solomkinmv.glossary.translateserviceclient.domain.TranslateResult;
import lombok.extern.slf4j.Slf4j;

import static java.util.Collections.emptyList;

@Slf4j
public class TranslateClientFallbackFactory implements FallbackFactory<TranslateClient> {

    @Override
    public TranslateClient create(Throwable cause) {
        return new TranslateClient() {
            @Override
            public TranslateResult translate(String text, Language source, Language translationDirection) {
                log.warn("Failed to get translation result [text: {}, source: {}, translationDirection: {}]",
                         text, source, translationDirection);
                return new TranslateResult(text, emptyList(), source, translationDirection);
            }

            @Override
            public TranslateResult translate(String text, Language translationDirection) {
                log.warn("Failed to get translation result [text: {}, translationDirection: {}]",
                         text, translationDirection);
                return new TranslateResult(text, emptyList(), null, translationDirection);
            }
        };
    }
}
