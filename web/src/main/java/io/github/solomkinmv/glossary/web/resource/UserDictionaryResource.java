package io.github.solomkinmv.glossary.web.resource;

import io.github.solomkinmv.glossary.persistence.model.UserDictionary;
import org.springframework.hateoas.ResourceSupport;

/**
 * HATEOAS resource for {@link UserDictionary}
 */
public class UserDictionaryResource extends ResourceSupport {

    private final UserDictionary userDictionary;

    public UserDictionaryResource(UserDictionary userDictionary) {
        this.userDictionary = userDictionary;

//        add(linkTo(UserDictionaryController.class).);
    }
}
