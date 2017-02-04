package io.github.solomkinmv.glossary.service.translate;

import lombok.Data;

import java.util.List;

/**
 * Stores translation result from {@link YandexTranslator}.
 */
@Data
class TranslationResult {
    private short code;
    private String lang;
    private List<String> text;
}
