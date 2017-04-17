package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.service.domain.UserService;
import io.github.solomkinmv.glossary.web.converter.ProfileConverter;
import io.github.solomkinmv.glossary.web.exception.EntryNotFoundException;
import io.github.solomkinmv.glossary.web.resource.ProfileResource;
import io.github.solomkinmv.glossary.web.security.annotation.CurrentUser;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by max on 04.01.17.
 * TODO: add JavaDoc
 */
@RestController
@Slf4j
public class ProfileController {

    private final UserService userService;
    private final ProfileConverter profileConverter;

    @Autowired
    public ProfileController(UserService userService, ProfileConverter profileConverter) {
        this.userService = userService;
        this.profileConverter = profileConverter;
    }

    @RequestMapping(value = "/api/me", method = RequestMethod.GET)
    public ProfileResource getCurrentUserProfile(@CurrentUser AuthenticatedUser user) {
        log.info("Getting current user profile");

        String username = user.getUsername();
        return userService.getByUsername(username)
                          .map(profileConverter::toDto)
                          .map(ProfileResource::new)
                          .orElseThrow(
                                  () -> new EntryNotFoundException("Couldn't find user with username: " + username));

    }
}
