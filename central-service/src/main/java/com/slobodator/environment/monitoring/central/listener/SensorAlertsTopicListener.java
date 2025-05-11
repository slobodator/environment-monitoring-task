package com.slobodator.environment.monitoring.central.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slobodator.environment.monitoring.central.event.SensorAlertEvent;
import com.slobodator.environment.monitoring.central.handler.SensorAlertEventHandler;
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
public class SensorAlertsTopicListener {
  ObjectMapper objectMapper;
  SensorAlertEventHandler sensorAlertEventHandler;

  @RetryableTopic(
      exclude = {
          JsonProcessingException.class
      }
  )
  @KafkaListener(
      topics = "${app.topics.sensor-alerts-topic}",
      groupId = "${spring.kafka.consumer.group-id}"
  )
  public void process(String eventStr) throws Exception {
    log.info("Received event '{}'", eventStr);
    var event = objectMapper.readValue(eventStr, SensorAlertEvent.class);
    sensorAlertEventHandler.handle(event);
  }
}
