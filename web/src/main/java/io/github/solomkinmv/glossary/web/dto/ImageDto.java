package io.github.solomkinmv.glossary.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Represents request with specified file name.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {
    @NotBlank
    private String image;
}
