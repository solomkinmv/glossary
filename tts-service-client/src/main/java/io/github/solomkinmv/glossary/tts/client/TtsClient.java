package io.github.solomkinmv.glossary.tts.client;

import io.github.solomkinmv.glossary.tts.client.domain.SpeechResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(
        name = "tts-service",
        path = "/tts-service"
)
public interface TtsClient {

    @GetMapping(value = "", produces = APPLICATION_JSON_VALUE)
    SpeechResult getSpeech(@RequestParam("text") String text);
}
