package com.slobodator.environment.monitoring.central.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slobodator.environment.monitoring.common.event.Event;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class EventPublisher {
  ObjectMapper objectMapper;
  KafkaTemplate<String, String> kafkaTemplate;

  @SneakyThrows
  public void send(String topic, Event event) {
    log.info("Sending '{}' to the topic '{}'", event, topic);
    kafkaTemplate.send(
        topic,
        event.key(),
        objectMapper.writeValueAsString(event)
    );
  }
}
