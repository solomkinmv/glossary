package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import io.github.solomkinmv.glossary.web.converter.WordSetConverter;
import io.github.solomkinmv.glossary.web.resource.WordSetResource;
import io.github.solomkinmv.glossary.web.security.annotation.CurrentUser;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * Endpoint for all operations with wordSet.
 */
@RestController
@RequestMapping("/api/sets")
public class WordSetController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WordSetController.class);

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
        LOGGER.info("Getting all wordSets.");
        return new Resources<>(wordSetService.listByUsername(user.getUsername())
                                             .stream()
                                             .map(wordSetConverter::toDto)
                                             .map(WordSetResource::new)
                                             .collect(Collectors.toList()));
    }

    /*@RequestMapping(value = "/{wordSetId}", method = RequestMethod.GET)
    public WordSetResource get(@PathVariable Long wordSetId) {
        LOGGER.info("Getting wordSet with id: {}", wordSetId);
        return wordSetService.getById(wordSetId)
                             .map(WordSetResource::new)
                             .orElseThrow(
                                     () -> new EntryNotFoundException("Couldn't find wordSet with id: " + wordSetId));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<WordSet> add(@RequestBody WordSet wordSet) {
        LOGGER.info("Creating wordSet: {}", wordSet);
        WordSet savedWordSet = wordSetService.save(wordSet);
        Link wordSetResource = new WordSetResource(savedWordSet).getLink("self");
        return ResponseEntity.created(URI.create(wordSetResource.getHref())).build();
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
