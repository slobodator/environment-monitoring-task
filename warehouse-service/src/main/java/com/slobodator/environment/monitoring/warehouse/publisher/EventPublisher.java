package com.slobodator.environment.monitoring.warehouse.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slobodator.environment.monitoring.common.event.Event;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderResult;

@Slf4j
@Component
@FieldDefaults(makeFinal = true)
public class EventPublisher {
  ObjectMapper objectMapper;
  ReactiveKafkaProducerTemplate<String, String> kafkaProducer;

  public EventPublisher(
      ObjectMapper objectMapper,
      KafkaProperties properties
  ) {
    this.objectMapper = objectMapper;

    this.kafkaProducer = new ReactiveKafkaProducerTemplate<>(
        SenderOptions.create(
            properties.buildProducerProperties()
        )
    );
  }

  @SneakyThrows
  public Mono<SenderResult<Void>> send(String topic, Event event) {
    log.info("Sending '{}' to the topic '{}'", event, topic);
    return kafkaProducer.send(
        topic,
        event.key(),
        objectMapper.writeValueAsString(event)
    );
  }
}
