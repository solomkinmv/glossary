package io.github.solomkinmv.glossary.statistics;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import reactor.core.publisher.Flux;

import java.time.Duration;

@SpringCloudApplication
@EnableBinding(Processor.class)
public class StatisticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatisticsServiceApplication.class, args);
    }

    //    @StreamListener
    public void receive1(@Input(Processor.INPUT) Flux<String> input) {
        input
                .delayElements(Duration.ofSeconds(2))
                .map(String::toUpperCase)
                .doOnEach(System.out::println)
                .subscribe();
    }

    @StreamListener(Processor.INPUT)
    public void receive2(String input) throws InterruptedException {
        Thread.sleep(2000);
        System.out.println(input.toUpperCase());
    }

}
