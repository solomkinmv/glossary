package io.github.solomkinmv.glossary.tts.controller;

import io.github.solomkinmv.glossary.tts.service.SpeechService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class SpeechController {

    private final SpeechService speechService;

    @Cacheable("speeches")
    @GetMapping
    public SpeechResult getSpeech(@RequestParam("text") String text) {
        return new SpeechResult(speechService.getSpeechRecord(text));
    }
}
