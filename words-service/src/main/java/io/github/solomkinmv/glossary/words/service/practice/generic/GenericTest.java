package io.github.solomkinmv.glossary.words.service.practice.generic;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.github.solomkinmv.glossary.words.persistence.domain.WordStage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)))
public class GenericTest {

    List<GenericTestWord> words;

    @Value
    @Builder
    public static class GenericTestWord {
        long wordId;
        String text;
        String translation;
        WordStage stage;
        String image;
        String sound;
    }
}
