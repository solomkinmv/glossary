package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.service.speach.SpeechService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/speech")
@Slf4j
public class SpeechController {
    private final SpeechService speechService;

    @Autowired
    public SpeechController(SpeechService speechService) {
        this.speechService = speechService;
    }

    // TODO: replace String with HATEAOS resource
    @GetMapping("")
    public ResponseEntity<String> getSpeechRecord(@RequestParam("text") String text) {
        log.info("Getting speech record for the following text: {}", text);
        String recordPath = speechService.getSpeechRecord(text);
        return ResponseEntity.ok(recordPath.toString());
    }
}
