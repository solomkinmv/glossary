package io.github.solomkinmv.glossary.web.resource;

import io.github.solomkinmv.glossary.web.controller.WordSetController;
import io.github.solomkinmv.glossary.web.dto.WordSetDto;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * HATEOAS resource for WordSet.
 */
@Getter
public class WordSetResource extends ResourceSupport {
    private final WordSetDto set;

    public WordSetResource(WordSetDto set) {
        this.set = set;

        add(linkTo(WordSetController.class).withRel("wordSets"));
        add(linkTo(methodOn(WordSetController.class).getWordSetById(null, set.getId())).withSelfRel());
    }
}
