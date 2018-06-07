package io.github.solomkinmv.glossary.words.service.practice.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.github.solomkinmv.glossary.words.service.practice.Answer;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.Set;

/**
 * Represents information necessary for the practice quiz. Created by {@link QuizPracticeService}.
 */
@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)))
public class Quiz {
    Set<Question> questions;

    @Value
    public static class Question {
        String questionText;
        Answer answer;
        Set<String> alternatives;
    }
}
