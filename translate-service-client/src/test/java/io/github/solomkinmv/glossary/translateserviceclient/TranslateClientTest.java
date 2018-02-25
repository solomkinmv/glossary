package io.github.solomkinmv.glossary.translateserviceclient;

import io.github.solomkinmv.glossary.translateserviceclient.domain.Language;
import io.github.solomkinmv.glossary.translateserviceclient.domain.TranslateResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static wiremock.org.eclipse.jetty.http.HttpStatus.OK_200;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TranslateClientTest.TestConfiguration.class)
@AutoConfigureWireMock(port = 8082)
public class TranslateClientTest {

    @Autowired
    private TranslateClient translateClient;

    @Test
    public void translatesTextWithSourceLanguage() {
        String text = "hello world";
        TranslateResult expectedResult = new TranslateResult(text, Collections.singletonList("привет мир"),
                                                             Language.ENGLISH, Language.RUSSIAN);
        stubFor(post(urlPathEqualTo("/translate-service/translate"))
                        .withQueryParam("text", equalTo(text))
                        .withQueryParam("source", equalTo("ENGLISH"))
                        .withQueryParam("target", equalTo("RUSSIAN"))
                        .willReturn(aResponse()
                                            .withStatus(OK_200)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody("{\n" +
                                                              "  \"sourceText\": \"hello world\",\n" +
                                                              "  \"result\": [\n" +
                                                              "    \"привет мир\"\n" +
                                                              "  ],\n" +
                                                              "  \"sourceLanguage\": \"ENGLISH\",\n" +
                                                              "  \"targetLanguage\": \"RUSSIAN\"\n" +
                                                              "}")));

        TranslateResult actualTranslateResult = translateClient.translate(text, Language.ENGLISH, Language.RUSSIAN);

        assertThat(actualTranslateResult).isEqualTo(expectedResult);
    }

    @Test
    public void translatesTextWithoutSourceLanguage() {
        String text = "hello world";
        TranslateResult expectedResult = new TranslateResult(text, Collections.singletonList("привет мир"),
                                                             Language.ENGLISH, Language.RUSSIAN);
        stubFor(post(urlPathEqualTo("/translate-service/translate"))
                        .withQueryParam("text", equalTo(text))
                        .withQueryParam("target", equalTo("RUSSIAN"))
                        .willReturn(aResponse()
                                            .withStatus(OK_200)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody("{\n" +
                                                              "  \"sourceText\": \"hello world\",\n" +
                                                              "  \"result\": [\n" +
                                                              "    \"привет мир\"\n" +
                                                              "  ],\n" +
                                                              "  \"sourceLanguage\": \"ENGLISH\",\n" +
                                                              "  \"targetLanguage\": \"RUSSIAN\"\n" +
                                                              "}")));

        TranslateResult actualTranslateResult = translateClient.translate(text, Language.RUSSIAN);

        assertThat(actualTranslateResult).isEqualTo(expectedResult);
    }

    @EnableAutoConfiguration
    public static class TestConfiguration {
    }
}