package io.github.solomkinmv.glossary.statistics.config;

import io.github.solomkinmv.glossary.statistics.metrics.QueueSize;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfiguration {

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.initialize();
        return rabbitAdmin;
    }

    @Bean
    public QueueSize queueSize(RabbitAdmin admin, @Value("${queue.name}") String queueName) {
        return new QueueSize(admin, queueName);
    }

    @Bean
    public Gauge queueSizeGauge(MeterRegistry meterRegistry, QueueSize queueSize) {
        return Gauge.builder("rabbitmq.queue_size", queueSize, QueueSize::getCurrentValue)
                    .register(meterRegistry);
    }
}
