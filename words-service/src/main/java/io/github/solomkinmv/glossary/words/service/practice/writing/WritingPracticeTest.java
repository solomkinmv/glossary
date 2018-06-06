package io.github.solomkinmv.glossary.words.service.practice.writing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.solomkinmv.glossary.words.service.practice.Answer;
import lombok.Value;

import java.util.Set;

@Value
public class WritingPracticeTest {
    Set<Question> questions;

    @JsonCreator
    public WritingPracticeTest(@JsonProperty("questions") Set<Question> questions) {
        this.questions = questions;
    }

    @Value
    public static class Question {
        String questionText;
        Answer answer;

        @JsonCreator
        public Question(@JsonProperty("questionText") String questionText, @JsonProperty("answer") Answer answer) {
            this.questionText = questionText;
            this.answer = answer;
        }


    }
}
