package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.persistence.model.Topic;
import io.github.solomkinmv.glossary.service.domain.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoint for all operations with topic.
 */
@RestController
@RequestMapping("/api/topics")
public class TopicController {
    private final Logger LOGGER = LoggerFactory.getLogger(TopicController.class);

    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @RequestMapping(method = RequestMethod.GET)
    List<Topic> topics() {
        LOGGER.info("Getting all topics.");
        return topicService.listAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    Topic createTopic(@RequestBody Topic topic) {
        LOGGER.info("Creating topic: {}", topic);
        return topicService.save(topic);
    }
}
