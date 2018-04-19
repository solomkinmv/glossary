package io.github.solomkinmv.glossary.words.persistence.repository;

import io.github.solomkinmv.glossary.words.persistence.domain.WordSet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WordSetRepository extends CrudRepository<WordSet, Long> {
    List<WordSet> findAllByUserId(long userId);
}
