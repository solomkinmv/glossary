package io.github.solomkinmv.glossary.words.service.statistic;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class StatisticService {

    private final Source statisticsSource;

    public void reportStats(LearningResultMessage learningResultMessage) {
        log.info("Reporting stats: {}", learningResultMessage);
        statisticsSource.output()
                        .send(MessageBuilder.withPayload(learningResultMessage).build());
    }

}
