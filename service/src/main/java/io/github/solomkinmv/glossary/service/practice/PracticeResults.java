package io.github.solomkinmv.glossary.service.practice;

import lombok.Value;

import java.util.Map;

@Value
public class PracticeResults {
    Map<Long, Boolean> wordAnswers;
}
