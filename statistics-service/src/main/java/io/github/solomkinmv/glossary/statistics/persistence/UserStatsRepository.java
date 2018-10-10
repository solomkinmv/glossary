package io.github.solomkinmv.glossary.statistics.persistence;

import io.github.solomkinmv.glossary.statistics.domain.UserStats;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserStatsRepository extends ReactiveCrudRepository<UserStats, String> {

    Mono<UserStats> findBySubjectId(Mono<String> subjectId);

    Mono<UserStats> findBySubjectId(String subjectId);
}
