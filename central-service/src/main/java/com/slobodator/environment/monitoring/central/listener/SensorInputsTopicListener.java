package com.slobodator.environment.monitoring.central.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slobodator.environment.monitoring.central.handler.SensorInputEventHandler;
import com.slobodator.environment.monitoring.common.event.SensorInputEvent;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class SensorInputsTopicListener {
  ObjectMapper objectMapper;
  SensorInputEventHandler sensorInputEventHandler;

  @RetryableTopic(
      exclude = {
          JsonProcessingException.class
      }
  )
  @KafkaListener(
      topics = "${app.topics.sensor-inputs-topic}",
      groupId = "${spring.kafka.consumer.group-id}"
  )
  public void process(String eventStr) throws Exception {
    log.info("Received event '{}'", eventStr);
    var event = objectMapper.readValue(eventStr, SensorInputEvent.class);
    sensorInputEventHandler.handle(event);
  }
}
