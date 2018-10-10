package io.github.solomkinmv.glossary.statistics.controller;

import io.github.solomkinmv.glossary.statistics.controller.dto.UserStatsResponse;
import io.github.solomkinmv.glossary.statistics.service.StatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
public class StatisticsController {

    private final StatisticsService statisticsService;

    @RequestMapping("/")
    Mono<UserStatsResponse> getUserStats(@RequestParam("subjectId") String subjectId) { // todo: use subject from token
        return statisticsService.getStats(Mono.just(subjectId))
                                .map(UserStatsResponse::valueOf);
    }
}
