package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.persistence.model.Topic;
import io.github.solomkinmv.glossary.persistence.model.Word;
import io.github.solomkinmv.glossary.service.domain.TopicService;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.web.dto.IdDto;
import io.github.solomkinmv.glossary.web.exception.EntryNotFoundException;
import io.github.solomkinmv.glossary.web.resource.TopicResource;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Endpoint for all operations with topic.
 */
@RestController
@RequestMapping("/api/topics")
public class TopicController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicController.class);

    private final TopicService topicService;
    private final WordService wordService;

    @Autowired
    public TopicController(TopicService topicService, WordService wordService) {
        this.topicService = topicService;
        this.wordService = wordService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Resources<TopicResource> topics() {
        LOGGER.info("Getting all topics.");
        return new Resources<>(topicService.listAll()
                                           .stream()
                                           .map(TopicResource::new)
                                           .collect(Collectors.toList()));
    }

    @RequestMapping(value = "/{topicId}", method = RequestMethod.GET)
    public TopicResource get(@PathVariable Long topicId) {
        LOGGER.info("Getting topic with id: {}", topicId);
        return topicService.getById(topicId)
                           .map(TopicResource::new)
                           .orElseThrow(() -> new EntryNotFoundException("Couldn't find topic with id: " + topicId));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Topic> add(@RequestBody Topic topic) {
        LOGGER.info("Creating topic: {}", topic);
        Topic savedTopic = topicService.save(topic);
        Link topicResource = new TopicResource(savedTopic).getLink("self");
        return ResponseEntity.created(URI.create(topicResource.getHref())).build();
    }

    @RequestMapping(value = "/{topicId}", method = RequestMethod.POST)
    public ResponseEntity<Topic> checkIfExist(@PathVariable Long topicId) {
        LOGGER.info("Checking if topic with id {} exist", topicId);
        if (topicService.getById(topicId).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/{topicId}", method = RequestMethod.DELETE)
    public ResponseEntity<Topic> delete(@PathVariable Long topicId) {
        LOGGER.info("Deleting word with id: {}", topicId);
        topicService.delete(topicId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{topicId}", method = RequestMethod.PUT)
    public ResponseEntity<Topic> update(@PathVariable Long topicId, @RequestBody Topic topic) {
        LOGGER.info("Updating topic: {}", topic);
        topic.setId(topicId);
        Topic savedTopic = topicService.update(topic);
        return ResponseEntity.ok(savedTopic);
    }

    @RequestMapping(value = "/{topicId}/words", method = RequestMethod.GET)
    public Resources<WordResource> words(@PathVariable Long topicId) {
        LOGGER.info("Getting all words for topic with id {}", topicId);

        Optional<Topic> optionalTopic = topicService.getById(topicId);

        Topic topic = optionalTopic.orElseThrow(
                () -> new EntryNotFoundException("Couldn't find topic with id: " + topicId));

        return new Resources<>(topic.getWords().stream()
                                    .map(WordResource::new)
                                    .collect(Collectors.toList()));
    }

    @RequestMapping(value = "/{topicId}/words", method = RequestMethod.POST)
    public ResponseEntity<TopicResource> addWord(@PathVariable Long topicId, @RequestBody IdDto idDto) {
        Long wordId = idDto.getId();
        LOGGER.info("Adding word (id = {}) to topic (id = {})", wordId, topicId);

        Topic topic = topicService.getById(topicId).orElseThrow(
                () -> new EntryNotFoundException("Couldn't find topic with id: " + topicId));

        Word word = wordService.getById(wordId).orElseThrow(
                () -> new EntryNotFoundException("Couldn't find word with id: " + wordId));

        List<Word> topicWords = topic.getWords();
        if (topicWords.contains(word)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        topicWords.add(word);
        Topic updatedTopic = topicService.update(topic);
        TopicResource topicResource = new TopicResource(updatedTopic);
        return ResponseEntity.ok().body(topicResource);
    }

    @RequestMapping(value = "/{topicId}/words/{wordId}", method = RequestMethod.DELETE)
    public ResponseEntity<Word> deleteWord(@PathVariable Long topicId, @PathVariable Long wordId) {
        LOGGER.info("Deleting word with id: {}", topicId);
        Topic topic = topicService.getById(topicId)
                                  .orElseThrow(
                                          () -> new EntryNotFoundException("Couldn't find topic with id: " + topicId));

        topic.getWords().removeIf(word -> word.getId().equals(wordId));
        Topic updatedTopic = topicService.update(topic);
        return ResponseEntity.ok().build();
    }

}
