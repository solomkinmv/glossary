package io.github.solomkinmv.glossary.web.dto;

import lombok.Value;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

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
