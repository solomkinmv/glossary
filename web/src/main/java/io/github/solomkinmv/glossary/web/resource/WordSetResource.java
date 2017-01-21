package io.github.solomkinmv.glossary.web.resource;

import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.web.controller.WordSetController;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * HATEOAS resource for WordSet.
 */
public class WordSetResource extends ResourceSupport {
    private final WordSet wordSet;

    public WordSetResource(WordSet wordSet) {
        this.wordSet = wordSet;

        add(linkTo(WordSetController.class).withRel("wordSets"));
        add(linkTo(methodOn(WordSetController.class).get(wordSet.getId())).withSelfRel());
    }

    public WordSet getWordSet() {
        return wordSet;
    }
}
