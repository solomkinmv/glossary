package io.github.solomkinmv.glossary.persistence.dao;

import io.github.solomkinmv.glossary.persistence.model.Word;

/**
 * Describes methods to interact with {@link Word} domain object.
 */
public interface WordDao extends CRUDService<Word, Long> {
}
