package io.github.solomkinmv.glossary.service.translate;

import io.github.solomkinmv.glossary.service.ServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * Test for {@link YandexTranslator}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceConfig.class)
public class YandexTranslatorTest {

    @Autowired
    private Translator translator;

    @Test
    public void execute() {
        String expectedTranslation = "Привет мир";

        String actualTranslation = translator.execute("Hello world", Language.ENGLISH, Language.RUSSIAN);

        assertEquals(expectedTranslation, actualTranslation);
    }

    @Test
    public void returnsEmptyStringIfTextIsNull() {
        String actualTranslation = translator.execute(null, Language.ENGLISH, Language.RUSSIAN);

        assertEquals("", actualTranslation);
    }
}