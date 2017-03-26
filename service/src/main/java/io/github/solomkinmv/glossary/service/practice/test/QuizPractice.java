package io.github.solomkinmv.glossary.service.practice.test;

import io.github.solomkinmv.glossary.service.dto.WordDto;
import lombok.Value;

import java.util.List;
import java.util.Map;

/**
 * Represents information necessary for the practice test. Created by {@link QuizPracticeService}.
 */
@Value
public class QuizPractice {
    Map<WordDto, Choices> words;

    @Value
    public static class Choices {
        List<WordDto> alternatives;
    }
}
