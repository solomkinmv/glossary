package io.github.solomkinmv.glossary.web.resource;

import io.github.solomkinmv.glossary.persistence.model.UserDictionary;
import io.github.solomkinmv.glossary.service.dto.DictionaryDto;
import io.github.solomkinmv.glossary.web.controller.UserDictionaryController;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * HATEOAS resource for {@link UserDictionary}
 */
public class UserDictionaryResource extends ResourceSupport {

    private final DictionaryDto userDictionary;

    public UserDictionaryResource(DictionaryDto userDictionary) {
        this.userDictionary = userDictionary;

        add(linkTo(UserDictionaryController.class).withSelfRel());
    }

    public DictionaryDto getUserDictionary() {
        return userDictionary;
    }
}
