package io.github.solomkinmv.glossary.web.security.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by max on 03.01.17.
 * TODO: add JavaDoc
 */
public class LoginRequest {
    private final String username;
    private final String password;

    /* TODO: read about @JsonProperty and @JsonCreator */
    @JsonCreator
    public LoginRequest(@JsonProperty String username, @JsonProperty String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
