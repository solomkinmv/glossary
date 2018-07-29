package io.github.solomkinmv.glossary.words.service.wordset;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)))
public class WordSetMeta {

    @NotBlank
    private String name;

    @NotBlank
    private String description;
}
