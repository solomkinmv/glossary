package io.github.solomkinmv.glossary.web.resource;

import io.github.solomkinmv.glossary.web.controller.PracticeController;
import io.github.solomkinmv.glossary.web.dto.WordDto;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class RepetitionResource extends ResourceSupport {
    private final List<WordDto> words;

    public RepetitionResource(List<WordDto> words, Long setId) {
        this.words = words;

        add(linkTo(methodOn(PracticeController.class).getWordsForRepetition(null, setId)).withSelfRel());
    }
}
