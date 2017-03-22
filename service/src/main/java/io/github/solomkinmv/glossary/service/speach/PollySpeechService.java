package io.github.solomkinmv.glossary.service.speach;

import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.model.*;
import io.github.solomkinmv.glossary.service.storage.StorageService;
import io.github.solomkinmv.glossary.service.storage.StoredType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.function.Supplier;

@Component
@Slf4j
public class PollySpeechService implements SpeechService {
    private static final OutputFormat FORMAT = OutputFormat.Mp3;
    private static final StoredType TYPE = StoredType.SOUND;
    private final AmazonPolly polly;
    private final StorageService storageService;
    private final Voice voice;

    @Autowired
    public PollySpeechService(AmazonPolly polly, StorageService storageService) {
        this.polly = polly;
        this.storageService = storageService;

        // Create describe voices request.
        DescribeVoicesRequest describeVoicesRequest = new DescribeVoicesRequest();

        // Synchronously ask Amazon Polly to describe available TTS voices.
        DescribeVoicesResult describeVoicesResult = polly.describeVoices(describeVoicesRequest);
        voice = describeVoicesResult.getVoices().get(0);
    }

    @Override
    public String getSpeechRecord(String speech) {
        log.info("Getting speech record");
        return storageService.getObject(speech, TYPE)
                             .orElseGet(createSpeechRecordSupplier(speech));
    }

    private Supplier<String> createSpeechRecordSupplier(String speech) {
        SynthesizeSpeechRequest synthReq = new SynthesizeSpeechRequest()
                .withText(speech)
                .withVoiceId(voice.getId())
                .withOutputFormat(FORMAT);
        SynthesizeSpeechResult synthRes = polly.synthesizeSpeech(synthReq);

        InputStream audioStream = synthRes.getAudioStream();
        String filename = speech.replace(' ', '_') + "." + FORMAT;

        return () -> storageService.store(audioStream, filename, TYPE);
    }
}