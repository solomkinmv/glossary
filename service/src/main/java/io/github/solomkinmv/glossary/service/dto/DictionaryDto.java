package io.github.solomkinmv.glossary.service.dto;

import io.github.solomkinmv.glossary.persistence.model.UserDictionary;
import io.github.solomkinmv.glossary.persistence.model.WordSet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * DTO used to represent {@link UserDictionary}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryDto {
    private Set<WordSet> wordSets;
}
