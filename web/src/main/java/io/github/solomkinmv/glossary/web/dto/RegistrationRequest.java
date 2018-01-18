package io.github.solomkinmv.glossary.web.dto;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
public class RegistrationRequest {

    @NotEmpty
    String name;
    @Size(min = 5, max = 20)
    String username;
    @Size(min = 5)
    String password;
    @Email
    String email;
}
