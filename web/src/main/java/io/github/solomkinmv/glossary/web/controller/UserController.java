package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.service.domain.UserDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
