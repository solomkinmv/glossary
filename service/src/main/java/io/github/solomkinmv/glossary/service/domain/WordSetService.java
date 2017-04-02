package io.github.solomkinmv.glossary.service.domain;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.persistence.model.WordSet;

import java.util.List;

/**
 * Describes methods to interact with {@link StudiedWord}.
 */
public interface WordSetService extends CRUDService<WordSet, Long> {
    List<WordSet> listByUsername(String username);
}
