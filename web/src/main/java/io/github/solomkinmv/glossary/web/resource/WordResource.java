package io.github.solomkinmv.glossary.web.resource;

import io.github.solomkinmv.glossary.persistence.model.Word;
import io.github.solomkinmv.glossary.web.controller.WordController;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * HATEOAS resource for word.
 */
public class WordResource extends ResourceSupport {
    private final Word word;

    public WordResource(Word word) {
        this.word = word;

        add(linkTo(WordController.class).withRel("words"));
        add(linkTo(methodOn(WordController.class).get(word.getId())).withSelfRel());
    }

    public Word getWord() {
        return word;
    }
}
