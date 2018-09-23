package io.github.solomkinmv.glossary.statistics.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class UserStats {
    private String subjectId;
    private int totalWords;
    private int totalLearnedWords;
    private LocalDate weekEnd;
    private Set<String> learnedWordsThisWeek;
    private Set<String> learningWordsThisWeek;
}
