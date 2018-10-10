package io.github.solomkinmv.glossary.statistics.listener;

import io.github.solomkinmv.glossary.statistics.service.StatisticsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Component
@AllArgsConstructor
@Slf4j
public class QueueListener {

    private final StatisticsService statisticsService;

    //    @StreamListener
    public void receive1(@Input(Processor.INPUT) Flux<String> input) {
        input
                .delayElements(Duration.ofSeconds(2))
                .map(String::toUpperCase)
                .doOnEach(System.out::println)
                .subscribe();
    }

    @StreamListener(Processor.INPUT)
    public void receive2(LearningResultMessage learningResultMessage) throws InterruptedException {
        Thread.sleep(2000);
        log.info("Processing message: {}", learningResultMessage);
        statisticsService.updateStats(learningResultMessage)
                         .doOnSuccess(stats -> log.info("Successfully updated stats! {}", stats))
                         .doOnError(error -> log.error("Failed to update stats: ", error))
                         .subscribe();
    }
}
