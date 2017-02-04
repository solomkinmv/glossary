package io.github.solomkinmv.glossary.service.domain;

import io.github.solomkinmv.glossary.persistence.model.Word;

import java.util.List;

/**
 * Describes methods to interact with {@link Word}.
 */
public interface WordService extends CRUDService<Word, Long> {
    List<Word> search(String text);
}
