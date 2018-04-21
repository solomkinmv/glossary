package io.github.solomkinmv.glossary.words.persistence.repository;

import io.github.solomkinmv.glossary.words.persistence.domain.AggregatedWord;
import org.springframework.data.repository.CrudRepository;

public interface AggregatedWordRepository extends CrudRepository<AggregatedWord, Long> {
}
