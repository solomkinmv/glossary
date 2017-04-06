package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import io.github.solomkinmv.glossary.web.converter.WordConverter;
import io.github.solomkinmv.glossary.web.converter.WordSetConverter;
import io.github.solomkinmv.glossary.web.dto.WordDto;
import io.github.solomkinmv.glossary.web.dto.WordSetDto;
import io.github.solomkinmv.glossary.web.dto.WordSetMetaDto;
import io.github.solomkinmv.glossary.web.exception.EntryNotFoundException;
import io.github.solomkinmv.glossary.web.resource.WordResource;
import io.github.solomkinmv.glossary.web.resource.WordSetResource;
import io.github.solomkinmv.glossary.web.security.annotation.CurrentUser;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.stream.Collectors;

/**
 * Endpoint for all operations with wordSet.
 */
@RestController
@RequestMapping("/api/sets")
@Slf4j
public class WordSetController {
    private final WordSetService wordSetService;
    private final WordSetConverter wordSetConverter;
    private final WordConverter wordConverter;

    @Autowired
    public WordSetController(WordSetService wordSetService, WordSetConverter wordSetConverter, WordConverter wordConverter) {
        this.wordSetService = wordSetService;
        this.wordSetConverter = wordSetConverter;
        this.wordConverter = wordConverter;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Resources<WordSetResource> getAllWordSets(@CurrentUser AuthenticatedUser user) {
        log.info("Getting all wordSets.");

        return new Resources<>(wordSetService.listByUsername(user.getUsername())
                                             .stream()
                                             .map(wordSetConverter::toDto)
                                             .map(WordSetResource::new)
                                             .collect(Collectors.toList()));
    }

    @RequestMapping(value = "/{wordSetId}", method = RequestMethod.GET)
    public WordSetResource getWordSetById(@CurrentUser AuthenticatedUser user, @PathVariable Long wordSetId) {
        log.info("Getting wordSet by id {}", wordSetId);

        return wordSetService.getByIdAndUsername(wordSetId, user.getUsername())
                             .map(wordSetConverter::toDto)
                             .map(WordSetResource::new)
                             .orElseThrow(() -> new EntryNotFoundException(
                                     "Can't find word set for " + user.getUsername() + " username and id " + wordSetId));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Void> createWordSet(@CurrentUser AuthenticatedUser user,
                                              @Validated @RequestBody WordSetDto wordSetDto) {
        log.info("Creating word set for user {}: {}", user.getUsername(), wordSetDto);
        WordSet wordSet = wordSetService.saveForUser(user.getUsername(), wordSetConverter.toModel(wordSetDto));
        Link self = new WordSetResource(wordSetConverter.toDto(wordSet)).getLink("self");
        return ResponseEntity.created(URI.create(self.getHref())).build();
    }

    @RequestMapping(value = "/{wordSetId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteWordSet(@CurrentUser AuthenticatedUser user, @PathVariable Long wordSetId) {
        log.info("Deleting word set with id {} for user {}", wordSetId, user.getUsername());
        wordSetService.deleteByIdAndUsername(wordSetId, user.getUsername());

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{wordSetId}/words/{wordId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteWordFromWordSet(@CurrentUser AuthenticatedUser user,
                                                      @PathVariable Long wordSetId,
                                                      @PathVariable Long wordId) {
        log.info("Deleting word (id {}) from word set (id {}) for {} user", wordId, wordSetId, user.getUsername());
        wordSetService.deleteWordFromWordSetByIdAndUsername(wordId, wordSetId, user.getUsername());

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{wordSetId}/words", method = RequestMethod.POST)
    public ResponseEntity<Void> createWordByAddingToWordSet(@CurrentUser AuthenticatedUser user,
                                                            @PathVariable Long wordSetId,
                                                            @Validated @RequestBody WordDto wordDto) {
        log.info("Adding word to word set (id {}) for user {}: {}", wordSetId, user.getUsername(), wordDto);
        StudiedWord studiedWord = wordConverter.toModel(wordDto);
        StudiedWord savedWord = wordSetService.addWordToWordSet(studiedWord, wordSetId, user.getUsername());
        Link self = new WordResource(wordConverter.toDto(savedWord)).getLink("self");

        return ResponseEntity.created(URI.create(self.getHref())).build();
    }

    @RequestMapping(value = "/{wordSetId}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateWordSetMetaInformation(@CurrentUser AuthenticatedUser user,
                                                             @PathVariable Long wordSetId,
                                                             @RequestBody WordSetMetaDto wordSetMetaDto) {
        log.info("Updating word set (id {}) meta information for user {}: {}",
                 wordSetId, user.getUsername(), wordSetMetaDto);

        WordSet wordSet = wordSetService
                .getByIdAndUsername(wordSetId, user.getUsername())
                .orElseThrow(() -> new EntryNotFoundException(
                        "Can't find word set with id " + wordSetId + " for user " + user.getUsername()));
        wordSetService.updateWordSetMeta(wordSet, wordSetMetaDto.getName(), wordSetMetaDto.getDescription());

        return ResponseEntity.ok().build();
    }
}
