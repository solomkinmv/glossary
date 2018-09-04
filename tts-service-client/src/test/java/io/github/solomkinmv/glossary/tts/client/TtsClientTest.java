package io.github.solomkinmv.glossary.tts.client;

import io.github.solomkinmv.glossary.tts.client.domain.SpeechResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static wiremock.org.eclipse.jetty.http.HttpStatus.OK_200;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TtsClientTest.TestConfiguration.class)
@AutoConfigureWireMock(port = 8083)
public class TtsClientTest {

    private static final String TOKEN = "token-value";

    @Autowired
    private TtsClient ttsClient;

    @Test
    public void generatesSpeechForText() {
        String text = "hello world";
        SpeechResult expectedSpeechResult = new SpeechResult("some url");

        stubFor(get(urlPathEqualTo("/"))
                        .withQueryParam("text", equalTo(text))
                        .willReturn(aResponse()
                                            .withStatus(OK_200)
                                            .withHeader("Content-Type", "application/json")
                                            .withHeader("Authorization", "Bearer " + TOKEN)
                                            .withBody("{\n  \"url\": \"some url\"\n}")));

        SpeechResult actualSpeechResult = ttsClient.getSpeech(text);

        assertThat(actualSpeechResult).isEqualTo(expectedSpeechResult);
    }

    @EnableAutoConfiguration
    public static class TestConfiguration {
    }
}