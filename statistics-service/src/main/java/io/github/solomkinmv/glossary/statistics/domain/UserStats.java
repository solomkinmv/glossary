package io.github.solomkinmv.glossary.statistics.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@Wither
public class UserStats {

    @Id
    private String id;
    private String subjectId;
    private LocalDate weekEnd;
    private Set<Long> learnedWordsThisWeek;
    private Set<Long> learningWordsThisWeek;

    public UserStats addLearnedWord(long wordId) {
        if (learnedWordsThisWeek.contains(wordId)) {
            return this;
        }

        Set<Long> newLearnedWords = new HashSet<>(learnedWordsThisWeek);
        newLearnedWords.add(wordId);
        Set<Long> newLearningWords = new HashSet<>(learningWordsThisWeek);
        newLearningWords.add(wordId);
        return this.withLearnedWordsThisWeek(newLearnedWords)
                   .withLearningWordsThisWeek(newLearningWords);
    }

    public UserStats removeLearnedWord(long wordId) {
        if (!learnedWordsThisWeek.contains(wordId)) {
            return this;
        }

        Set<Long> newLearnedWords = new HashSet<>(learnedWordsThisWeek);
        newLearnedWords.remove(wordId);
        return this.withLearnedWordsThisWeek(newLearnedWords);
    }

    public UserStats addLearningWord(long wordId) {
        if (learnedWordsThisWeek.contains(wordId)) {
            return this;
        }

        Set<Long> newLearningWords = new HashSet<>(learningWordsThisWeek);
        newLearningWords.add(wordId);
        return this.withLearningWordsThisWeek(newLearningWords);
    }
}
