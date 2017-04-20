package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.service.statistics.StatisticGenerator;
import io.github.solomkinmv.glossary.web.resource.StatisticResource;
import io.github.solomkinmv.glossary.web.security.annotation.CurrentUser;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistic")
@Slf4j
public class StatisticController {

    private final StatisticGenerator statisticGenerator;

    @Autowired
    public StatisticController(StatisticGenerator statisticGenerator) {
        this.statisticGenerator = statisticGenerator;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public StatisticResource getStatistic(@CurrentUser AuthenticatedUser user) {
        String username = user.getUsername();
        log.info("Get statistic for user: {}", username);

        return new StatisticResource(statisticGenerator.generate(username));
    }
}
