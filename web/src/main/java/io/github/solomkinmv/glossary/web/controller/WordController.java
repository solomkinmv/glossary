package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.persistence.model.Word;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.web.exception.EntryNotFoundException;
import io.github.solomkinmv.glossary.web.resource.WordResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.stream.Collectors;

/**
 * Endpoint for all operations with word.
 */
@RestController
@RequestMapping("/api/words")
public class WordController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WordController.class);

    private final WordService wordService;

    @Autowired
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Resources<WordResource> words() {
        LOGGER.info("Getting all words");

        return new Resources<>(wordService.listAll()
                                          .stream()
                                          .map(WordResource::new)
                                          .collect(Collectors.toList()));
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Resources<WordResource> search(@RequestParam("query") String query) {
        return new Resources<>(wordService.search(query)
                                          .stream()
                                          .map(WordResource::new)
                                          .collect(Collectors.toList()));
    }

    @RequestMapping(value = "/{wordId}", method = RequestMethod.GET)
    public WordResource get(@PathVariable Long wordId) {
        LOGGER.info("Getting word with id: {}", wordId);
        return wordService.getById(wordId)
                          .map(WordResource::new)
                          .orElseThrow(() -> new EntryNotFoundException("Couldn't find word with id: " + wordId));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Word> add(@RequestBody Word word) {
        LOGGER.info("Creating word: {}", word);
        Word savedWord = wordService.save(word);
        Link wordResource = new WordResource(savedWord).getLink("self");
        return ResponseEntity.created(URI.create(wordResource.getHref())).build();
    }

    @RequestMapping(value = "/{wordId}", method = RequestMethod.POST)
    public ResponseEntity<?> checkIfExist(@PathVariable Long wordId) {
        LOGGER.info("Checking if word with id {} exist", wordId);
        if (wordService.getById(wordId).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/{wordId}", method = RequestMethod.DELETE)
    public ResponseEntity<Word> delete(@PathVariable Long wordId) {
        LOGGER.info("Deleting word with id: {}", wordId);
        wordService.delete(wordId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{wordId}", method = RequestMethod.PUT)
    public ResponseEntity<Word> update(@PathVariable Long wordId, @RequestBody Word word) {
        LOGGER.info("Updating word: {}", word);
        word.setId(wordId);
        Word savedWord = wordService.update(word);
        return ResponseEntity.ok(savedWord);
    }
}
