package io.github.solomkinmv.glossary.words.service.external;

import io.github.solomkinmv.glossary.tts.client.TtsClient;
import io.github.solomkinmv.glossary.tts.client.domain.SpeechResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TtsFacade {

    private final TtsClient ttsClient;

    public String getSpeechUrl(String text) {
        SpeechResult result = ttsClient.getSpeech(text);

        return result.getUrl();
    }
}
