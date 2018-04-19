package io.github.solomkinmv.glossary.words.persistence.repository;

import io.github.solomkinmv.glossary.words.persistence.domain.StudiedWord;
import org.springframework.data.repository.CrudRepository;

public interface StudiedWordRepository extends CrudRepository<StudiedWord, Long> {
}
