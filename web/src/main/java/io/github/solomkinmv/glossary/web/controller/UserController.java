package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.persistence.model.UserDictionary;
import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.service.domain.UserDictionaryService;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

/**
 * Endpoint for all operations with user.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserDictionaryService userDictionaryService;


    @Autowired
    public UserController(UserDictionaryService userDictionaryService) {
        this.userDictionaryService = userDictionaryService;
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    String test() {
        return "hello";
    }

    @RequestMapping(value = "/wordSets", method = RequestMethod.GET)
    @ResponseBody
    WordSet wordSets(Principal principal) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) ((Authentication) principal).getPrincipal();

        String username = authenticatedUser.getUsername();
        Set<WordSet> wordSets = userDictionaryService.getByUsername(username)
                                                     .map(UserDictionary::getWordSets)
                                                     .orElseThrow(() -> new UsernameNotFoundException(
                                                         "No such username: " + username));
        Optional<WordSet> wordSet = wordSets.stream()
                                            .findAny();

        return wordSet.orElse(null);
    }
}
