package io.github.solomkinmv.glossary.web.converter;

import io.github.solomkinmv.glossary.persistence.model.UserDictionary;
import io.github.solomkinmv.glossary.web.dto.DictionaryDto;
import org.springframework.stereotype.Component;

/**
 * Converter used to convert between {@link UserDictionary} and {@link DictionaryDto}.
 */
@Component
public class UserDictionaryConverter {
    public DictionaryDto toDto(UserDictionary userDictionary) {
        return new DictionaryDto(userDictionary.getWordSets());
    }
}
