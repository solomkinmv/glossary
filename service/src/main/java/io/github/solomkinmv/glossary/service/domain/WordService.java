package io.github.solomkinmv.glossary.service.domain;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.persistence.model.Word;
import io.github.solomkinmv.glossary.persistence.model.WordStage;

import java.util.List;
import java.util.Optional;

/**
 * Describes methods to interact with {@link StudiedWord}.
 */
public interface WordService extends CreateService<StudiedWord>, ReadService<StudiedWord, Long>, DeleteService<Long> {
    List<StudiedWord> listByUsername(String username);

    List<Word> search(String query);

    Optional<Word> findByText(String text);

    Optional<StudiedWord> getWordByIdAndUsername(Long wordId, String username);

    void updateWordMeta(StudiedWord word, WordStage stage, String image);
}
