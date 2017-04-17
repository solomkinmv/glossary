package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.persistence.model.User;
import io.github.solomkinmv.glossary.service.domain.UserService;
import io.github.solomkinmv.glossary.web.converter.ProfileConverter;
import io.github.solomkinmv.glossary.web.dto.ProfileMetaDto;
import io.github.solomkinmv.glossary.web.exception.EntryNotFoundException;
import io.github.solomkinmv.glossary.web.resource.ProfileResource;
import io.github.solomkinmv.glossary.web.security.annotation.CurrentUser;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileController(UserService userService, ProfileConverter profileConverter, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.profileConverter = profileConverter;
        this.passwordEncoder = passwordEncoder;
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

    @RequestMapping(value = "/api/me", method = RequestMethod.POST)
    public ResponseEntity<Void> updateProfile(@CurrentUser AuthenticatedUser authenticatedUser,
                                              @Validated @RequestBody ProfileMetaDto profileMeta) {
        String username = authenticatedUser.getUsername();
        log.info("Updating profile info (username={}): ", username, profileMeta);

        userService.getByUsername(username)
                   .map(user -> updateUser(user, profileMeta))
                   .map(userService::update)
                   .orElseThrow(() -> new EntryNotFoundException("Couldn't find user with username: " + username));

        return ResponseEntity.ok().build();
    }

    private User updateUser(User user, ProfileMetaDto profileMeta) {
        user.setName(profileMeta.getName());
        user.setEmail(profileMeta.getEmail());
        String pass = passwordEncoder.encode(profileMeta.getPassword());
        user.setPassword(pass);
        return user;
    }
}
