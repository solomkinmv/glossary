package io.github.solomkinmv.glossary.web.converter;

import io.github.solomkinmv.glossary.persistence.model.User;
import io.github.solomkinmv.glossary.web.dto.ProfileDto;
import org.springframework.stereotype.Component;

@Component
public class ProfileConverter {
    public ProfileDto toDto(User user) {
        return new ProfileDto(
                user.getName(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
