package io.github.solomkinmv.glossary.web.converter;

import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.web.dto.WordSetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class WordSetConverter {
    private final WordConverter wordConverter;

    @Autowired
    public WordSetConverter(WordConverter wordConverter) {
        this.wordConverter = wordConverter;
    }

    public WordSetDto toDto(WordSet wordSet) {
        return new WordSetDto(
                wordSet.getId(),
                wordSet.getName(),
                wordSet.getDescription(),
                wordSet.getStudiedWords().stream()
                       .map(wordConverter::toDto)
                       .collect(Collectors.toList())
        );
    }
}
