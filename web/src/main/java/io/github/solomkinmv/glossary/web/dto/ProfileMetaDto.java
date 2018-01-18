package io.github.solomkinmv.glossary.web.dto;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
public class ProfileMetaDto {

    @NotEmpty
    String name;
    @Email
    String email;
    @Size(min = 5)
    String password;
}
