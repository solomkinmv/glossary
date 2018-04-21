package io.github.solomkinmv.glossary.words.service.word;

import io.github.solomkinmv.glossary.words.persistence.domain.StudiedWord;
import io.github.solomkinmv.glossary.words.persistence.repository.StudiedWordRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudiedWordService {

    private final StudiedWordRepository studiedWordRepository;

    public StudiedWord save(WordMeta wordMeta) {
        StudiedWord studiedWord = new StudiedWord(wordMeta.getText(), wordMeta.getTranslation());
        return studiedWordRepository.save(studiedWord);
    }
}
