package io.github.solomkinmv.glossary.service.practice;

import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Value
public class PracticeResults {
    @NotNull
    Map<Long, Boolean> wordAnswers;
}
