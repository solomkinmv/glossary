package io.github.solomkinmv.glossary.statistics.controller.dto;

import io.github.solomkinmv.glossary.statistics.domain.UserStats;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserStatsResponse {
    private String subjectId;
    private int learningWords;
    private int learnedWords;

    public static UserStatsResponse valueOf(UserStats userStats) {
        return UserStatsResponse.builder()
                                .subjectId(userStats.getSubjectId())
                                .learningWords(userStats.getLearningWordsThisWeek().size())
                                .learnedWords(userStats.getLearnedWordsThisWeek().size())
                                .build();
    }
}
