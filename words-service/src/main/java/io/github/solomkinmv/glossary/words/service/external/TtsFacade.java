package io.github.solomkinmv.glossary.words.service.external;

import io.github.solomkinmv.glossary.tts.client.TtsClient;
import io.github.solomkinmv.glossary.tts.client.domain.SpeechResult;
import io.github.solomkinmv.glossary.words.exception.WordsServiceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class TtsFacade {

    private final TtsClient ttsClient;

    public String getSpeechUrl(String text) {
        log.debug("Getting speech record for test: {}", text);

        SpeechResult result;
        try {
            result = ttsClient.getSpeech(text);
        } catch (RuntimeException e) {
            String msg = String.format("Failed to get speech record for text: %s", text);
            log.error(msg);
            throw new WordsServiceException(msg, e);
        }

        return result.getUrl();
    }
}
