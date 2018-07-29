package io.github.solomkinmv.glossary.words.service.word;

import io.github.solomkinmv.glossary.words.exception.DomainObjectNotFound;
import io.github.solomkinmv.glossary.words.persistence.domain.Word;
import io.github.solomkinmv.glossary.words.persistence.repository.WordRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WordService {

    private final WordRepository wordRepository;

    public Word getById(long wordId) {
        return wordRepository.findById(wordId)
                             .orElseThrow(() -> new DomainObjectNotFound(
                                     "Can't find word with id " + wordId));
    }

    public List<Word> findAllForUser(String subjectId) {
        return wordRepository.findAllBySubjectId(subjectId);
    }

    public void save(Word word) {
        wordRepository.save(word);
    }
}
