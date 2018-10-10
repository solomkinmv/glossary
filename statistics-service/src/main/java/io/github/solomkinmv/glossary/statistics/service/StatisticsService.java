package io.github.solomkinmv.glossary.statistics.service;

import io.github.solomkinmv.glossary.statistics.domain.UserStats;
import io.github.solomkinmv.glossary.statistics.domain.WordStage;
import io.github.solomkinmv.glossary.statistics.listener.LearningResultMessage;
import io.github.solomkinmv.glossary.statistics.persistence.UserStatsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.function.Function;

import static java.util.Collections.emptySet;

@Service
@AllArgsConstructor
public class StatisticsService {

    private final UserStatsRepository userStatsRepository;

    public Mono<UserStats> getStats(Mono<String> subjectId) {
        return userStatsRepository.findBySubjectId(subjectId
//                                                           .delayElement(Duration.ofMillis(randomDelay()))
        )
                                  .switchIfEmpty(subjectId.map(this::createDefaultUserStats));
    }

    public Mono<UserStats> updateStats(LearningResultMessage learningResult) {
        return userStatsRepository.findBySubjectId(learningResult.getSubjectId())
                                  .switchIfEmpty(Mono.defer(() -> Mono.just(createDefaultUserStats(learningResult.getSubjectId()))))
                                  .map(updateWordsStats(learningResult))
                                  .flatMap(userStatsRepository::save);

    }

    private Function<UserStats, UserStats> updateWordsStats(LearningResultMessage learningResult) {
        return userStats -> {
            Long wordId = learningResult.getWordId();

            if (learningResult.getStage() == WordStage.NOT_LEARNED &&
                    userStats.getLearnedWordsThisWeek().contains(wordId)) {

                return userStats.removeLearnedWord(wordId);
            }

            if (learningResult.getStage() == WordStage.LEARNED) {
                return userStats.addLearnedWord(wordId);
            }

            return userStats.addLearningWord(wordId);
        };
    }

    private UserStats createDefaultUserStats(String subjectId) {
        return UserStats.builder()
                        .learnedWordsThisWeek(emptySet())
                        .learningWordsThisWeek(emptySet())
                        .subjectId(subjectId)
                        .weekEnd(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)))
                        .build();
    }

    private int randomDelay() {
        int minimum = 300;
        int maximum = 3000;
        return minimum + (int) (Math.random() * (maximum - minimum));
    }
}
