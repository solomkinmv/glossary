package io.github.solomkinmv.glossary.web.dto;

/**
 * Created by max on 05.01.17.
 * TODO: add JavaDoc
 */
public class RegistrationRequest {
    private String username;
    private String password;
    private String details;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
