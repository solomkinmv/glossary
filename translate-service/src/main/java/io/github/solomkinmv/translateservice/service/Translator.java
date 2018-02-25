package io.github.solomkinmv.translateservice.service;

import io.github.solomkinmv.translateservice.domain.Language;
import io.github.solomkinmv.translateservice.domain.TranslateResult;

import java.util.Optional;

/**
 * Interface describes methods to interact with Translate service.
 */
public interface Translator {

    /**
     * Translates text from {@code source} language to {@code target} language.
     *
     * @param text   the original text
     * @param source {@link Language} of the original text
     * @param target {@link Language} of the target text
     * @return translated text
     */
    Optional<TranslateResult> execute(String text, Language source, Language target);

    Optional<TranslateResult> execute(String text, Language translationDirection);
}
