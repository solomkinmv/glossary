package io.github.solomkinmv.glossary.statistics.service;

import io.github.solomkinmv.glossary.statistics.domain.UserStats;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class StatisticsService {

    public Mono<UserStats> getStats(Mono<String> subjectId) {
        return subjectId
                .delaySubscription(Duration.ofMillis(randomDelay()))
                .map(subj -> UserStats.builder()
                                      .subjectId(subj)
                                      .totalLearnedWords(10)
                                      .totalWords(100)
                                      .build());
    }

    private int randomDelay() {
        int minimum = 300;
        int maximum = 3000;
        return minimum + (int) (Math.random() * (maximum - minimum));
    }
}
