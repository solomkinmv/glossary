package io.github.solomkinmv.glossary.web.resource;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.web.controller.WordController;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * HATEOAS resource for word.
 */
@Getter
public class StudiedWordResource extends ResourceSupport {
    private final StudiedWord studiedWord;

    public StudiedWordResource(StudiedWord studiedWord) {
        this.studiedWord = studiedWord;

        add(linkTo(WordController.class).withRel("words"));
//        add(linkTo(methodOn(WordController.class).get(word.getId())).withSelfRel());
    }
}
