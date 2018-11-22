package io.github.solomkinmv.glossary.statistics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.solomkinmv.glossary.statistics.service.StatisticsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;

@Component
@AllArgsConstructor
@Slf4j
public class QueueListener {

    private final StatisticsService statisticsService;
    private final ObjectMapper objectMapper;

    //    @StreamListener
    public void receive1(@Input(Processor.INPUT) Flux<String> input) {
        input
                .delayElements(Duration.ofSeconds(2))
                .map(String::toUpperCase)
                .doOnEach(System.out::println)
                .subscribe();
    }

    @StreamListener(Processor.INPUT)
    public void receive2(String message) throws InterruptedException {
        log.info("Processing message: {}", message);
        Thread.sleep(1000);
        log.info("Processed message: {}", message);

        LearningResultMessage learningResultMessage = null;
        try {
            learningResultMessage = objectMapper.readValue(message, LearningResultMessage.class);
        } catch (IOException e) {
            log.error("Failed to parse: {}", message, e);
        }

        statisticsService.updateStats(learningResultMessage)
                         .doOnSuccess(stats -> log.info("Successfully updated stats! {}", stats))
                         .doOnError(error -> log.error("Failed to update stats: ", error))
                         .subscribe();
    }
}
