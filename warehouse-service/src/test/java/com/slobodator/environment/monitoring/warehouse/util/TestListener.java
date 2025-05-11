package com.slobodator.environment.monitoring.warehouse.util;

import static java.util.Collections.emptyList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestListener {
  private final ObjectMapper objectMapper;
  private final Map<String, List<JsonNode>> events = new ConcurrentHashMap<>();

  @SuppressWarnings("unused")
  @RetryableTopic
  @KafkaListener(
      topics = {
          "${app.topics.sensor-inputs-topic}"
      },
      groupId = "testListener"
  )
  @SneakyThrows
  public void receive(ConsumerRecord<String, String> consumerRecord) {
    var nodes = events.computeIfAbsent(
        consumerRecord.topic(),
        t -> new CopyOnWriteArrayList<>()
    );
    nodes.add(objectMapper.readValue(consumerRecord.value(), JsonNode.class));
  }

  @SneakyThrows
  public <T> List<T> getEvents(String topic, Class<T> clazz) {
    return events.getOrDefault(topic, emptyList())
        .stream()
        .map(jsonNode -> convertToEventBody(jsonNode, clazz))
        .toList();
  }

  @SneakyThrows
  public void reset() {
    events.clear();
  }

  @SneakyThrows
  private <T> T convertToEventBody(JsonNode jsonNode, Class<T> clazz) {
    return objectMapper.treeToValue(jsonNode, clazz);
  }
}
