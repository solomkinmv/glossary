package io.github.solomkinmv.glossary.words.controller;

import io.github.solomkinmv.glossary.words.controller.dto.WordSetResponse;
import io.github.solomkinmv.glossary.words.service.WordSetMeta;
import io.github.solomkinmv.glossary.words.service.WordSetService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/word-sets")
@AllArgsConstructor
public class WordSetController {

    private final WordSetService wordSetService;

    @GetMapping("/")
    public List<WordSetResponse> getAllByUserId(@RequestParam("userId") long userId) {
        log.info("Getting all word sets for userId: {}", userId);
        return wordSetService.findAllForUserId(userId).stream()
                             .map(WordSetResponse::of)
                             .collect(Collectors.toList());
    }

    @PostMapping("/")
    public void createWordSet(@RequestParam("userId") long userId,
                              @RequestBody WordSetMeta wordSetMeta) {
        long wordSetId = wordSetService.create(wordSetMeta);
    }

    // todo: create
    // todo: delete
    // todo: updateWordSetMetaInformation
    // todo: createWordByAddingToWordSet
    // todo: deleteWordFromWordSet
    // todo: createWordByAddingToWordSet
    // todo: deleteWordFromWordSet
}
