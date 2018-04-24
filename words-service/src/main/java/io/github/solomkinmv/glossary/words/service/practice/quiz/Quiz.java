package io.github.solomkinmv.glossary.words.service.practice.quiz;

import io.github.solomkinmv.glossary.words.service.practice.Answer;
import lombok.Value;

import java.util.Set;

/**
 * Represents information necessary for the practice quiz. Created by {@link QuizPracticeService}.
 */
@Value
public class Quiz {
    Set<Question> questions;

    @Value
    public static class Question {
        String questionText;
        Answer answer;
        Set<String> alternatives;
    }
}
