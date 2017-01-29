package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.service.domain.UserDictionaryService;
import io.github.solomkinmv.glossary.web.exception.EntryNotFoundException;
import io.github.solomkinmv.glossary.web.resource.UserDictionaryResource;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Endpoint for all operations with user dictionary.
 */
@RestController
@RequestMapping("/api/dictionary")
public class UserDictionaryController {

    private final UserDictionaryService userDictionaryService;

    @Autowired
    public UserDictionaryController(UserDictionaryService userDictionaryService) {
        this.userDictionaryService = userDictionaryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public UserDictionaryResource get(Principal principal) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) ((Authentication) principal).getPrincipal();

        String username = authenticatedUser.getUsername();

        return userDictionaryService
                .getByUsername(username)
                .map(UserDictionaryResource::new)
                .orElseThrow(() -> new EntryNotFoundException(
                        "Couldn't find user dictionary by following username: " + username));
    }
}
