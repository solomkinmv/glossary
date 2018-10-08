package io.github.solomkinmv.glossary.statistics.metrics;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

import java.util.Properties;

import static org.springframework.amqp.rabbit.core.RabbitAdmin.QUEUE_MESSAGE_COUNT;

@Slf4j
@AllArgsConstructor
public class QueueSize {
    private final RabbitAdmin admin;
    private final String queueName;

    public double getCurrentValue() {
        Properties queueProperties = admin.getQueueProperties(queueName);
        int messageCount = Integer.parseInt(queueProperties.get(QUEUE_MESSAGE_COUNT).toString());
        log.info("{} has {} messages", queueName, messageCount);
        System.out.println(queueName + " has " + messageCount + " messages");

        return messageCount;
    }
}
