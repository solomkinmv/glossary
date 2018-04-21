package io.github.solomkinmv.glossary.words.persistence.repository;

import io.github.solomkinmv.glossary.words.persistence.domain.WordSet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WordSetRepository extends CrudRepository<WordSet, Long> {

    List<WordSet> findAllByUserId(long userId);

    @Query("SELECT w FROM WordSet w LEFT JOIN FETCH w.studiedWords WHERE w.id = (:id)")
    Optional<WordSet> findByUserId(@Param("id") long id);

}
