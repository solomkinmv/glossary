package io.github.solomkinmv.glossary.statistics.service;

import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@AllArgsConstructor
public class DemoService {

    private final Processor learnedWords;

    public void send(int number) {
        Flux.range(0, number)
            .map(i -> "message " + i)
            .map(MessageBuilder::withPayload)
            .map(MessageBuilder::build)
            .subscribe(learnedWords.output()::send);
    }

}
