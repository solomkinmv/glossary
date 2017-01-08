package io.github.solomkinmv.glossary.persistence.model;

/**
 * Created by max on 02.01.17.
 * TODO: add JavaDoc
 */
public enum RoleType {
    ADMIN, USER;

    public String authority() {
        return "ROLE_" + this.name();
    }
}
