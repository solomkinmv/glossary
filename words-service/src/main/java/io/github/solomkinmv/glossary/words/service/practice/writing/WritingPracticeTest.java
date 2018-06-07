package io.github.solomkinmv.glossary.words.service.practice.writing;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.github.solomkinmv.glossary.words.service.practice.Answer;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.Set;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)))
public class WritingPracticeTest {
    Set<Question> questions;

    @Value
    public static class Question {
        String questionText;
        Answer answer;

    }
}
