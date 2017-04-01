package io.github.solomkinmv.glossary.service.domain;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.persistence.model.Word;

import java.util.List;

/**
 * Describes methods to interact with {@link StudiedWord}.
 */
public interface WordService extends CreateService<StudiedWord>, ReadService<StudiedWord, Long>, DeleteService<Long> {
    List<StudiedWord> listByUsername(String username);

    List<Word> search(String query);
}
