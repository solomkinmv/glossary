package io.github.solomkinmv.glossary.translateserviceclient;

import io.github.solomkinmv.glossary.translateserviceclient.domain.Language;
import io.github.solomkinmv.glossary.translateserviceclient.domain.TranslateResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static wiremock.org.eclipse.jetty.http.HttpStatus.OK_200;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TranslateClientTest.TestConfiguration.class)
@AutoConfigureWireMock(port = 8082)
public class TranslateClientTest {

    private static final String TOKEN = "token-value";

    @Autowired
    private TranslateClient translateClient;

    @Mock
    private OAuth2Authentication authentication;

    @Before
    public void setUp() {
        initializeSecurityContext();
    }

    @Test
    public void translatesTextWithSourceLanguage() {
        String text = "hello world";
        TranslateResult expectedResult = new TranslateResult(text, Collections.singletonList("привет мир"),
                                                             Language.ENGLISH, Language.RUSSIAN);
        stubFor(post(urlPathEqualTo("/translate"))
                        .withQueryParam("text", equalTo(text))
                        .withQueryParam("source", equalTo("ENGLISH"))
                        .withQueryParam("target", equalTo("RUSSIAN"))
                        .willReturn(aResponse()
                                            .withStatus(OK_200)
                                            .withHeader("Content-Type", "application/json")
                                            .withHeader("Authorization", "Bearer " + TOKEN)
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
        stubFor(post(urlPathEqualTo("/translate"))
                        .withQueryParam("text", equalTo(text))
                        .withQueryParam("target", equalTo("RUSSIAN"))
                        .willReturn(aResponse()
                                            .withStatus(OK_200)
                                            .withHeader("Content-Type", "application/json")
                                            .withHeader("Authorization", "Bearer " + TOKEN)
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

    private void initializeSecurityContext() {
        when(authentication.getDetails())
                .thenReturn(new OAuth2AuthenticationDetails(new MockHttpServletRequest()) {
                    @Override
                    public String getTokenValue() {
                        return TOKEN;
                    }
                });

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @EnableAutoConfiguration
    public static class TestConfiguration {
    }
}