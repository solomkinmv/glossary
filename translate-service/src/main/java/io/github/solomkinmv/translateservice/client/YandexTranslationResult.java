package io.github.solomkinmv.translateservice.client;

import io.github.solomkinmv.translateservice.service.YandexTranslator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Stores translation result from {@link YandexTranslator}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YandexTranslationResult {
    private short code;
    private String lang;
    private List<String> text;
}
