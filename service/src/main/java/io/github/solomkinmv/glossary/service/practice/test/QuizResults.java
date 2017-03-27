package io.github.solomkinmv.glossary.service.practice.test;

import lombok.Value;

import java.util.Map;

@Value
public class QuizResults {
    Map<Long, Boolean> wordAnswers;
}
