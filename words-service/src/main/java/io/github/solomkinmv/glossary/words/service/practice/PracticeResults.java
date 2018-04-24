package io.github.solomkinmv.glossary.words.service.practice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PracticeResults {

    private Map<Long, Boolean> wordAnswers;
}
