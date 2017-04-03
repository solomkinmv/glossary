package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import io.github.solomkinmv.glossary.web.converter.WordSetConverter;
import io.github.solomkinmv.glossary.web.dto.WordSetDto;
import io.github.solomkinmv.glossary.web.exception.EntryNotFoundException;
import io.github.solomkinmv.glossary.web.resource.WordSetResource;
import io.github.solomkinmv.glossary.web.security.annotation.CurrentUser;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
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
    private final WordService wordService;
    private final WordSetConverter wordSetConverter;

    @Autowired
    public WordSetController(WordSetService wordSetService, WordService wordService, WordSetConverter wordSetConverter) {
        this.wordSetService = wordSetService;
        this.wordService = wordService;
        this.wordSetConverter = wordSetConverter;
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
    public ResponseEntity<Void> createWordSet(@CurrentUser AuthenticatedUser user, @RequestBody WordSetDto wordSetDto) {
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

    /*@RequestMapping(value = "/{wordSetId}", method = RequestMethod.GET)
    public WordSetResource get(@PathVariable Long wordSetId) {
        LOGGER.info("Getting wordSet with id: {}", wordSetId);
        return wordSetService.getById(wordSetId)
                             .map(WordSetResource::new)
                             .orElseThrow(
                                     () -> new EntryNotFoundException("Couldn't find wordSet with id: " + wordSetId));
    }

    @RequestMapping(value = "/{wordSetId}", method = RequestMethod.POST)
    public ResponseEntity<WordSet> checkIfExist(@PathVariable Long wordSetId) {
        LOGGER.info("Checking if wordSet with id {} exist", wordSetId);
        if (wordSetService.getById(wordSetId).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/{wordSetId}", method = RequestMethod.DELETE)
    public ResponseEntity<WordSet> delete(@PathVariable Long wordSetId) {
        LOGGER.info("Deleting word with id: {}", wordSetId);
        wordSetService.delete(wordSetId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{wordSetId}", method = RequestMethod.PUT)
    public ResponseEntity<WordSet> update(@PathVariable Long wordSetId, @RequestBody WordSet wordSet) {
        LOGGER.info("Updating wordSet: {}", wordSet);
        wordSet.setId(wordSetId);
        WordSet savedWordSet = wordSetService.update(wordSet);
        return ResponseEntity.ok(savedWordSet);
    }

    @RequestMapping(value = "/{wordSetId}/words", method = RequestMethod.GET)
    public Resources<StudiedWordResource> words(@PathVariable Long wordSetId) {
        LOGGER.info("Getting all studiedWords for wordSet with id {}", wordSetId);

        Optional<WordSet> optionalWordSet = wordSetService.getById(wordSetId);

        WordSet wordSet = optionalWordSet.orElseThrow(
                () -> new EntryNotFoundException("Couldn't find wordSet with id: " + wordSetId));

        return new Resources<>(wordSet.getStudiedWords().stream()
                                      .map(StudiedWordResource::new)
                                      .collect(Collectors.toList()));
    }

    @RequestMapping(value = "/{wordSetId}/words", method = RequestMethod.POST)
    public ResponseEntity<WordSetResource> addWord(@PathVariable Long wordSetId, @RequestBody IdDto idDto) {
        Long wordId = idDto.getId();
        LOGGER.info("Adding word (id = {}) to wordSet (id = {})", wordId, wordSetId);

        WordSet wordSet = wordSetService.getById(wordSetId).orElseThrow(
                () -> new EntryNotFoundException("Couldn't find wordSet with id: " + wordSetId));

        StudiedWord word = wordService.getById(wordId).orElseThrow(
                () -> new EntryNotFoundException("Couldn't find studiedWord with id: " + wordId));

        List<StudiedWord> wordSetStudiedWords = wordSet.getStudiedWords();
        if (wordSetStudiedWords.stream().anyMatch(studiedWord -> studiedWord.getId().equals(word.getId()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        wordSetStudiedWords.add(word);
        WordSet updatedWordSet = wordSetService.update(wordSet);
        WordSetResource wordSetResource = new WordSetResource(updatedWordSet);
        return ResponseEntity.ok().body(wordSetResource);
    }

    @RequestMapping(value = "/{wordSetId}/words/{wordId}", method = RequestMethod.DELETE)
    public ResponseEntity<StudiedWord> deleteWord(@PathVariable Long wordSetId, @PathVariable Long wordId) {
        LOGGER.info("Deleting word with id: {}", wordSetId);
        WordSet wordSet = wordSetService.getById(wordSetId)
                                        .orElseThrow(
                                                () -> new EntryNotFoundException(
                                                        "Couldn't find wordSet with id: " + wordSetId));

        wordSet.getStudiedWords().removeIf(word -> word.getId().equals(wordId));
        wordSetService.update(wordSet);
        return ResponseEntity.ok().build();
    }*/

}
