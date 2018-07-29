package io.github.solomkinmv.glossary.words.controller;

import io.github.solomkinmv.glossary.words.controller.dto.WordResponse;
import io.github.solomkinmv.glossary.words.service.word.WordMeta;
import io.github.solomkinmv.glossary.words.service.word.WordService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/words")
@AllArgsConstructor
public class WordController {

    private final WordService wordService;

    @GetMapping("{wordId}")
    public WordResponse getById(@PathVariable long wordId) {
        log.info("Getting word by id: {}", wordId);

        return WordResponse.of(wordService.getById(wordId));
    }

    @PatchMapping("{wordId}")
    public WordResponse update(@PathVariable long wordId, @RequestBody WordMeta wordMeta) {
        log.info("Updating word with id {}, wordMeta: {}", wordId, wordMeta);

        return WordResponse.of(wordService.updateWord(wordId, wordMeta));
    }
}
