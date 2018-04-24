package io.github.solomkinmv.glossary.words.persistence.repository;

import io.github.solomkinmv.glossary.words.persistence.domain.Word;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordRepository extends CrudRepository<Word, Long> {

    @Query("SELECT w FROM WordSet ws JOIN FETCH ws.words w WHERE ws.userId = (:userId)")
    List<Word> findAllByUserId(@Param("userId") long userId);
}
