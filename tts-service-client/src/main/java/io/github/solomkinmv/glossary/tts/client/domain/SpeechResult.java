package io.github.solomkinmv.glossary.tts.client.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class SpeechResult {

    String url;

    @JsonCreator
    public SpeechResult(@JsonProperty("url") String url) {
        this.url = url;
    }
}
