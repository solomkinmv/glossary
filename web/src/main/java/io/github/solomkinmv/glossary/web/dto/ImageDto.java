package io.github.solomkinmv.glossary.web.dto;

import lombok.Value;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Represents request with specified file name.
 */
@Value
public class ImageDto {
    @NotBlank
    String image;
}
