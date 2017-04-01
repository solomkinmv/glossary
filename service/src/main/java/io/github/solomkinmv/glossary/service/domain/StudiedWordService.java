package io.github.solomkinmv.glossary.service.domain;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;

import java.util.List;

/**
 * Describes methods to interact with {@link StudiedWord}.
 */
public interface StudiedWordService extends CreateService<StudiedWord>, ReadService<StudiedWord, Long>, DeleteService<Long> {
    List<StudiedWord> listByUsername(String username);
}
