package io.github.solomkinmv.glossary.service.practice.quiz;

import io.github.solomkinmv.glossary.persistence.model.WordStage;
import lombok.Value;

import java.net.URL;
import java.util.Set;

/**
 * Represents information necessary for the practice quiz. Created by {@link QuizPracticeService}.
 */
@Value
public class Quiz {
    Set<Question> questions;

    @Value
    public static class Answer {
        long wordId;
        String answer;
        WordStage stage;
        URL image;
        URL pronunciation;
    }

    @Value
    public static class Question {
        String questionText;
        Answer answer;
        Set<String> alternatives;
    }
}
