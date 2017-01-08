package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import io.github.solomkinmv.glossary.web.security.model.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by max on 04.01.17.
 * TODO: add JavaDoc
 */
@RestController
public class ProfileController {
    @RequestMapping(value = "/api/me", method = RequestMethod.GET)
    @ResponseBody
    public AuthenticatedUser get(JwtAuthenticationToken token) {
        return (AuthenticatedUser) token.getPrincipal();
    }
}
