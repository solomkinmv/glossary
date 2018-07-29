package io.github.solomkinmv.glossary.words.persistence.repository;

import io.github.solomkinmv.glossary.words.persistence.domain.WordSet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WordSetRepository extends CrudRepository<WordSet, Long> {

    List<WordSet> findAllBySubjectId(String subjectId);

    @Query("SELECT w FROM WordSet w LEFT JOIN FETCH w.words WHERE w.id = (:id)")
    Optional<WordSet> findById(@Param("id") long id);

}
