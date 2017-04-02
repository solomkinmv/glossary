package io.github.solomkinmv.glossary.persistence.dao;

import io.github.solomkinmv.glossary.persistence.model.WordSet;

import java.util.List;

/**
 * Describes methods to interact with {@link WordSet} domain object.
 */
public interface WordSetDao extends CRUDDao<WordSet, Long> {
    List<WordSet> listByUsername(String username);
}
