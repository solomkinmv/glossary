package io.github.solomkinmv.glossary.web.resource;

import io.github.solomkinmv.glossary.web.controller.ImageController;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class ImageSearchResource extends ResourceSupport {
    private final String query;
    private final List<String> images;

    public ImageSearchResource(String query, List<String> images) {
        this.query = query;
        this.images = images;

        add(linkTo(methodOn(ImageController.class).search(this.query)).withSelfRel());
    }
}
