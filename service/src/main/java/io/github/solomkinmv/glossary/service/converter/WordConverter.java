package io.github.solomkinmv.glossary.service.converter;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.persistence.model.Word;
import io.github.solomkinmv.glossary.service.dto.WordDto;
import org.springframework.stereotype.Component;

@Component
public class WordConverter {
    public WordDto toDto(StudiedWord studiedWord) {
        Word word = studiedWord.getWord();
        return new WordDto(
                studiedWord.getId(),
                word.getText(),
                word.getTranslation(),
                studiedWord.getStage(),
                word.getImage(),
                word.getSound());
    }
}
