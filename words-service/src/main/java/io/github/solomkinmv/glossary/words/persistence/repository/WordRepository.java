package io.github.solomkinmv.glossary.words.persistence.repository;

import io.github.solomkinmv.glossary.words.persistence.domain.Word;
import org.springframework.data.repository.CrudRepository;

public interface WordRepository extends CrudRepository<Word, Long> {
}
