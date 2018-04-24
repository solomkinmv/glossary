package io.github.solomkinmv.glossary.words.service.practice.writing;

import io.github.solomkinmv.glossary.words.service.practice.Answer;
import lombok.Value;

import java.util.Set;

@Value
public class WritingPracticeTest {
    Set<Question> questions;

    @Value
    public static class Question {
        String questionText;
        Answer answer;
    }
}
