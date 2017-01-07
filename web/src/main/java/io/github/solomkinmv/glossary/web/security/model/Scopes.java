package io.github.solomkinmv.glossary.web.security.model;

/**
 * Contains additional authority scopes (roles).
 */
public enum Scopes {
    REFRESH_TOKEN;

    public String authority() {
        return "ROLE_" + name();
    }
}
