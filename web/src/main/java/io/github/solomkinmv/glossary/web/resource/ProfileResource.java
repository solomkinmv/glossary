package io.github.solomkinmv.glossary.web.resource;

import io.github.solomkinmv.glossary.web.controller.*;
import io.github.solomkinmv.glossary.web.dto.ProfileDto;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class ProfileResource extends ResourceSupport {
    private final ProfileDto profile;

    public ProfileResource(ProfileDto profile) {
        this.profile = profile;

        add(linkTo(methodOn(ProfileController.class).getCurrentUserProfile(null)).withSelfRel());
        add(linkTo(WordController.class).withRel("words"));
        add(linkTo(WordSetController.class).withRel("sets"));
        add(linkTo(PracticeController.class).withRel("practices"));
        add(linkTo(ImageController.class).withRel("images"));
    }
}
