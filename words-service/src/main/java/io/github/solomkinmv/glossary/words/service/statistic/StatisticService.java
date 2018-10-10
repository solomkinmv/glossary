package io.github.solomkinmv.glossary.words.service.statistic;

import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StatisticService {

    private final Source statisticsSource;

    public void reportStats(LearningResultMessage learningResultMessage) {
        statisticsSource.output()
                        .send(MessageBuilder.withPayload(learningResultMessage).build());
    }

}
