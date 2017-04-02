package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.web.converter.WordConverter;
import io.github.solomkinmv.glossary.web.exception.EntryNotFoundException;
import io.github.solomkinmv.glossary.web.resource.WordResource;
import io.github.solomkinmv.glossary.web.security.annotation.CurrentUser;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * Endpoint for all operations with word.
 */
@RestController
@RequestMapping("/api/words")
@Slf4j
public class WordController {
    private final WordService wordService;
    private final WordConverter wordConverter;

    @Autowired
    public WordController(WordService wordService, WordConverter wordConverter) {
        this.wordService = wordService;
        this.wordConverter = wordConverter;
    }

    @GetMapping("")
    public Resources<WordResource> getAllWords(@CurrentUser AuthenticatedUser user) {
        log.info("Getting all words for user {}", user.getUsername());

        return new Resources<>(wordService.listByUsername(user.getUsername())
                                          .stream()
                                          .map(wordConverter::toDto)
                                          .map(WordResource::new)
                                          .collect(Collectors.toList()));
    }

    @RequestMapping(value = "/{wordId}", method = RequestMethod.GET)
    public WordResource getWordById(@CurrentUser AuthenticatedUser user, @PathVariable Long wordId) {
        log.info("Getting word by id {}", wordId);

        return wordService.getWordByIdAndUsername(wordId, user.getUsername())
                          .map(wordConverter::toDto)
                          .map(WordResource::new)
                          .orElseThrow(() -> new EntryNotFoundException("Couldn't find word with id: " + wordId));
    }
}
