package io.github.solomkinmv.translateservice.service;

import io.github.solomkinmv.translateservice.client.YandexTranslateClient;
import io.github.solomkinmv.translateservice.client.YandexTranslationResult;
import io.github.solomkinmv.translateservice.domain.Language;
import io.github.solomkinmv.translateservice.domain.TranslateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Optional;


/**
 * Implementation of {@link Translator} for the Yandex Translate service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class YandexTranslator implements Translator {
    private final YandexTranslateClient yandexTranslateClient;
    @Value("${translate.key}")
    private String key;

    @Override
    public Optional<TranslateResult> execute(String text, Language source, Language target) {
        return translate(text, source.getLanguage() + "-" + target.getLanguage())
                .map(result -> new TranslateResult(text, result.getText(), source, target));
    }

    @Override
    public Optional<TranslateResult> execute(String text, Language target) {
        return translate(text, target.getLanguage())
                .map(result -> new TranslateResult(text, result.getText(),
                                                   extractSourceLanguageAfterTranslation(result.getLang()), target));
    }

    private Language extractSourceLanguageAfterTranslation(String languages) {
        String shortSourceLanguage = languages.substring(0, 2);
        return Language.fromString(shortSourceLanguage).orElse(null);
    }

    private Optional<YandexTranslationResult> translate(String text, String lang) {
        if (StringUtils.isEmpty(text)) {
            return Optional.empty();
        }

        YandexTranslationResult translationResult = yandexTranslateClient.translate(key, text, lang);
        log.debug("Received translation result [text: {}, lang: {}, result: {}]", text, lang, translationResult);

        if (CollectionUtils.isEmpty(translationResult.getText())) {
            return Optional.empty();
        }
        String translatedText = translationResult.getText().get(0);
        if (translatedText.equals(text)) {
            return Optional.empty();
        }

        return Optional.of(translationResult);
    }
}
