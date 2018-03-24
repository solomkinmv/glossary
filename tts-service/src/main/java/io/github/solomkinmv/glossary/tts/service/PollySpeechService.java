package io.github.solomkinmv.glossary.tts.service;

import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.model.*;
import io.github.solomkinmv.glossary.tts.storage.StorageServiceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class PollySpeechService implements SpeechService {
    private static final OutputFormat FORMAT = OutputFormat.Mp3;
    private final AmazonPolly polly;
    private final StorageServiceFacade storageService;
    private Voice voice;

    @PostConstruct
    private void init() {
        // Create describe voices request
        DescribeVoicesRequest describeVoicesRequest = new DescribeVoicesRequest();

        // Synchronously ask Amazon Polly to describe available TTS voices.
        DescribeVoicesResult describeVoicesResult = polly.describeVoices(describeVoicesRequest);
        voice = describeVoicesResult.getVoices().get(0);
    }

    @Override
    public String getSpeechRecord(String speech) {
        log.info("Getting speech record");
        String filename = adaptToFilename(speech);
        return storageService.get(filename)
                             .orElseGet(createSpeechRecordSupplier(speech, filename));
    }

    private Supplier<String> createSpeechRecordSupplier(String speech, String filename) {
        return () -> {
            log.info("Creating new speech record: \"{}\"", speech);
            SynthesizeSpeechRequest synthReq = new SynthesizeSpeechRequest()
                    .withText(speech)
                    .withVoiceId(voice.getId())
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