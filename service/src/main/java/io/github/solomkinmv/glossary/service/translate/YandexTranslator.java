package io.github.solomkinmv.glossary.service.translate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Implementation of {@link Translator} for the Yandex Translate service.
 */
@Service
public class YandexTranslator implements Translator {
    private static final Logger LOGGER = LoggerFactory.getLogger(YandexTranslator.class);
    private static final String YANDEX_TRANSLATE_ENDPOINT = "https://translate.yandex.net/api/v1.5/tr.json/translate";
    private final ObjectMapper objectMapper;
    @Value("${translate.key}")
    private String key;

    @Autowired
    public YandexTranslator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String execute(String text, Language source, Language target) {
        try {
            URI uri = new URIBuilder(YANDEX_TRANSLATE_ENDPOINT)
                    .addParameter("key", key)
                    .addParameter("lang", source.getLanguage() + "-" + target.getLanguage())
                    .addParameter("text", text)
                    .build();
            String content = Request.Get(uri).execute().returnContent().asString();

            return objectMapper.readValue(content, TranslationResult.class).getText().get(0);
        } catch (URISyntaxException | IOException e) {
            String errorMessage = "Couldn't translate text: " + text;
            LOGGER.error(errorMessage, e);
            throw new TranslatorException(errorMessage, e);
        }
    }
}
