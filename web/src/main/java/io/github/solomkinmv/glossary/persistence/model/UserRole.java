package io.github.solomkinmv.glossary.persistence.model;

/**
 * Created by max on 02.01.17.
 * TODO: add JavaDoc
 */
public class UserRole {
    private final Long id;
    private final Role role;

    public UserRole(Long id, Role role) {
        this.id = id;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }
}
