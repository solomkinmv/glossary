package io.github.solomkinmv.glossary.service.domain;

import io.github.solomkinmv.glossary.persistence.model.UserDictionary;
import io.github.solomkinmv.glossary.service.dto.DictionaryDto;

import java.util.Optional;

/**
 * Describes methods to interact with {@link UserDictionary}.
 */
public interface UserDictionaryService extends CRUDService<UserDictionary, Long> {

    Optional<DictionaryDto> getByUsername(String username);
}
