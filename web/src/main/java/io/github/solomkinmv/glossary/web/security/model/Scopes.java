package io.github.solomkinmv.glossary.web.security.model;

/**
 * Created by max on 03.01.17.
 * TODO: add JavaDoc
 */
public enum Scopes {
    REFRESH_TOKEN;

    public String authority() {
        return "ROLE_" + name();
    }
}
