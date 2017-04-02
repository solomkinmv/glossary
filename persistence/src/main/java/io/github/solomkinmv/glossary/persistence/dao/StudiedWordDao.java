package io.github.solomkinmv.glossary.persistence.dao;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;

import java.util.List;
import java.util.Optional;

/**
 * Describes methods to interact with {@link StudiedWord} domain object.
 */
public interface StudiedWordDao extends CRUDDao<StudiedWord, Long> {
    List<StudiedWord> listAllByUsername(String username);

    Optional<StudiedWord> findByIdAndUsername(long wordId, String username);
}
