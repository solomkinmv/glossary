package io.github.solomkinmv.glossary.persistence.model;

import java.util.List;

/**
 * Created by max on 02.01.17.
 * TODO: add JavaDoc
 */
public class User {
    private Long id;
    private String username;
    private String password;
    private List<UserRole> roles;

    public User() {
    }

    public User(Long id, String username, String password, List<UserRole> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<UserRole> getRoles() {
        return roles;
    }
}
