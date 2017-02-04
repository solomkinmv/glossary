package io.github.solomkinmv.glossary.persistence.dao;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.persistence.model.Word;

import java.util.List;

/**
 * Describes methods to interact with {@link StudiedWord} domain object.
 */
public interface WordDao extends CRUDDao<Word, Long> {
    List<Word> search(String text);
}
