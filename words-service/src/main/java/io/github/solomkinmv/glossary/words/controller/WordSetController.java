package io.github.solomkinmv.glossary.words.controller;

import io.github.solomkinmv.glossary.words.controller.dto.WordSetResponse;
import io.github.solomkinmv.glossary.words.persistence.domain.WordSet;
import io.github.solomkinmv.glossary.words.service.word.WordMeta;
import io.github.solomkinmv.glossary.words.service.wordset.WordSetMeta;
import io.github.solomkinmv.glossary.words.service.wordset.WordSetService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/word-sets")
@AllArgsConstructor
public class WordSetController {

    private final WordSetService wordSetService;

    @GetMapping("/{wordSetId}")
    public WordSetResponse getById(@PathVariable long wordSetId) {
        log.info("Getting word set by id: {}", wordSetId);
        WordSet wordSet = wordSetService.getWordSet(wordSetId);
        log.info("Got word set by id: {} {}", wordSetId, wordSet);
        return WordSetResponse.of(wordSet);
    }

    @GetMapping("/")
    public List<WordSetResponse> getAllByUserId(@RequestParam("userId") long userId) {
        log.info("Getting all word sets for userId: {}", userId);
        return wordSetService.findAllForUserId(userId).stream()
                             .map(WordSetResponse::of)
                             .collect(Collectors.toList());
    }

    @PostMapping("/")
    public long createWordSet(@Validated @RequestBody WordSetMeta wordSetMeta) {
        log.info("Creating word set [wordSetMeta: {}]", wordSetMeta);
        long wordSetId = wordSetService.create(wordSetMeta);
        log.info("Created word set with id: {} [wordSetMeta: {}]", wordSetId, wordSetMeta);
        return wordSetId;
    }

    @PostMapping("/{wordSetId}/words")
    public WordSetResponse createWordByAddingToWordSet(@PathVariable long wordSetId,
                                                       @Validated @RequestBody WordMeta wordMeta) {
        log.info("Adding word to word set with id {}: {}", wordSetId, wordMeta);
        WordSet wordSet = wordSetService.addWordToWordSet(wordSetId, wordMeta);

        log.info("Added word {} to word set: {} [wordSetId: {}]", wordMeta, wordSet, wordSetId);
        return WordSetResponse.of(wordSet);
    }

    @DeleteMapping("/{wordSetId}")
    public void deleteWordSetById(@PathVariable long wordSetId) {
        log.info("Deleting word set by id: {}", wordSetId);

        wordSetService.deleteWordSetById(wordSetId);

        log.info("Deleted word set by id: {}", wordSetId);
    }

    @PatchMapping("/{wordSetId}")
    public WordSetResponse updateWordSetMetaInformation(@PathVariable long wordSetId,
                                                        @Validated @RequestBody WordSetMeta wordSetMeta) {
        log.info("Updating word set meta information [wordSetId: {}, wordSetMeta: {}]", wordSetId, wordSetMeta);

        WordSet wordSet = wordSetService.updateWordSetMetaInformation(wordSetId, wordSetMeta);

        log.info("Updated word set meta information: {}", wordSet);
        return WordSetResponse.of(wordSet);
    }

    // todo: delete
    // todo: updateWordSetMetaInformation
    // todo: deleteWordFromWordSet
}
