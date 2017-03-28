package io.github.solomkinmv.glossary.service.practice.quiz;

import lombok.Value;

import java.util.Map;

@Value
public class QuizResults {
    Map<Long, Boolean> wordAnswers;
}
