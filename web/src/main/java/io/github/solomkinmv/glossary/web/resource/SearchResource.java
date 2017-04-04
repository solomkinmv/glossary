package io.github.solomkinmv.glossary.web.resource;

import io.github.solomkinmv.glossary.service.search.SearchResult;
import io.github.solomkinmv.glossary.web.controller.WordController;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Getter
public class SearchResource extends ResourceSupport {
    private final SearchResult result;

    public SearchResource(SearchResult result) {
        this.result = result;

        add(linkTo(WordController.class).withRel("words"));

    }
}
