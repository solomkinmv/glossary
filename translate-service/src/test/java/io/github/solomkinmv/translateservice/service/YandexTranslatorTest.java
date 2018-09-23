package io.github.solomkinmv.translateservice.service;

import io.github.solomkinmv.translateservice.client.YandexTranslateClient;
import io.github.solomkinmv.translateservice.client.YandexTranslationResult;
import io.github.solomkinmv.translateservice.domain.Language;
import io.github.solomkinmv.translateservice.domain.TranslateResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class YandexTranslatorTest {

    @Mock
    private YandexTranslateClient yandexTranslateClient;
    private YandexTranslator yandexTranslator;

    @Before
    public void setUp() {
        yandexTranslator = new YandexTranslator(yandexTranslateClient);
    }

    @Test
    public void returnsEmptyIfTranslateClientReturnedNoResults() {
        when(yandexTranslateClient.translate(null, "text", Language.ENGLISH.getLanguage()))
                .thenReturn(new YandexTranslationResult((short) 200, Language.ENGLISH.getLanguage(), emptyList()));

        Optional<TranslateResult> translateResult = yandexTranslator.execute("text", Language.ENGLISH);

        assertThat(translateResult).isEmpty();
    }
}