package io.github.solomkinmv.glossary.tts.service;

import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import io.github.solomkinmv.glossary.tts.storage.StorageServiceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class PollySpeechService implements SpeechService {
    private static final OutputFormat FORMAT = OutputFormat.Mp3;
    private final AmazonPolly polly;
    private final StorageServiceFacade storageService;
    private String voiceId = "Joanna";

    @Override
    public String getSpeechRecord(String speech) {
        log.info("Getting speech record");
        String filename = adaptToFilename(speech);
        return storageService.get(filename)
                             .orElseGet(createSpeechRecordSupplier(speech, filename));
    }

    private Supplier<String> createSpeechRecordSupplier(String speech, String filename) {
        return () -> {
            log.info("Creating new speech record [speech: {}, filename: {}]", speech, filename);
            SynthesizeSpeechRequest synthReq = new SynthesizeSpeechRequest()
                    .withText(speech)
                    .withVoiceId(voiceId)
                    .withOutputFormat(FORMAT);
            SynthesizeSpeechResult synthRes = polly.synthesizeSpeech(synthReq);

            InputStream audioStream = synthRes.getAudioStream();
            return storageService.save(audioStream, filename);
        };
    }

    private String adaptToFilename(String speech) {
        return speech.replaceAll("\\W", "") + "." + FORMAT;
    }
}