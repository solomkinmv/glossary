package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.persistence.model.UserDictionary;
import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.service.domain.UserDictionaryService;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import io.github.solomkinmv.glossary.web.converter.UserDictionaryConverter;
import io.github.solomkinmv.glossary.web.dto.IdDto;
import io.github.solomkinmv.glossary.web.exception.EntryNotFoundException;
import io.github.solomkinmv.glossary.web.resource.UserDictionaryResource;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDictionaryController.class);
    private final UserDictionaryService userDictionaryService;
    private final WordSetService wordSetService;
    private final UserDictionaryConverter userDictionaryConverter;

    @Autowired
    public UserDictionaryController(UserDictionaryService userDictionaryService, WordSetService wordSetService, UserDictionaryConverter userDictionaryConverter) {
        this.userDictionaryService = userDictionaryService;
        this.wordSetService = wordSetService;
        this.userDictionaryConverter = userDictionaryConverter;
    }

    @RequestMapping(method = RequestMethod.GET)
    public UserDictionaryResource get(Principal principal) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) ((Authentication) principal).getPrincipal();

        String username = authenticatedUser.getUsername();

        return userDictionaryService
                .getByUsername(username)
                .map(userDictionaryConverter::toDto)
                .map(UserDictionaryResource::new)
                .orElseThrow(() -> new EntryNotFoundException(
                        "Couldn't find user dictionary by following username: " + username));
    }

    @RequestMapping(value = "/wordSets", method = RequestMethod.POST)
    public ResponseEntity<UserDictionaryResource> addWordSet(@RequestBody IdDto idDto, Principal principal) {
        LOGGER.info("Adding word set to dictionary");
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) ((Authentication) principal).getPrincipal();
        String username = authenticatedUser.getUsername();

        WordSet wordSet = wordSetService.getById(idDto.getId())
                                        .orElseThrow(() -> new EntryNotFoundException(
                                                "Couldn't find wordSet by id: " + idDto.getId()));
        UserDictionary userDictionary = userDictionaryService.getByUsername(username)
                                                             .orElseThrow(() -> new EntryNotFoundException(
                                                                     "Couldn't find dictionary by following username: " + username));

        userDictionary.getWordSets().add(wordSet);

        userDictionaryService.update(userDictionary);
        UserDictionaryResource userDictionaryResource = new UserDictionaryResource(
                userDictionaryConverter.toDto(userDictionary));

        return ResponseEntity.ok(userDictionaryResource);
    }
}
