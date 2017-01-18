package io.github.solomkinmv.glossary.web.resource;

import io.github.solomkinmv.glossary.persistence.model.Topic;
import io.github.solomkinmv.glossary.web.controller.TopicController;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * HATEOAS resource for Topic.
 */
public class TopicResource extends ResourceSupport {
    private final Topic topic;

    public TopicResource(Topic topic) {
        this.topic = topic;

        add(linkTo(TopicController.class).withRel("topics"));
        add(linkTo(methodOn(TopicController.class).get(topic.getId())).withSelfRel());
    }

    public Topic getTopic() {
        return topic;
    }
}
