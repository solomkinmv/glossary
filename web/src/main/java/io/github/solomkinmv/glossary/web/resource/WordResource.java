package io.github.solomkinmv.glossary.web.resource;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.web.controller.WordController;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * HATEOAS resource for word.
 */
@Getter
public class WordResource extends ResourceSupport {
    private final StudiedWord word;

    public WordResource(StudiedWord word) {
        this.word = word;

        add(linkTo(WordController.class).withRel("words"));
        add(linkTo(methodOn(WordController.class).getWordById(word.getId())).withSelfRel());
    }
}
