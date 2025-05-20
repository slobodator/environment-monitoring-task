package com.slobodator.environment.monitoring.warehouse.handler;

import com.slobodator.environment.monitoring.common.model.SensorType;
import com.slobodator.environment.monitoring.warehouse.config.AppConfig;
import com.slobodator.environment.monitoring.warehouse.mapper.SensorInputEventMapper;
import com.slobodator.environment.monitoring.warehouse.mapper.SensorInputParser;
import com.slobodator.environment.monitoring.warehouse.mapper.SensorInputParsingException;
import com.slobodator.environment.monitoring.warehouse.publisher.EventPublisher;
import io.netty.channel.socket.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class SensorInputHandler {
  AppConfig appConfig;
  SensorInputParser sensorInputParser;
  EventPublisher eventPublisher;
  SensorInputEventMapper sensorInputEventMapper;

  public Mono<String> handle(DatagramPacket datagramPacket, SensorType sensorType) {
    var inputStr = datagramPacket.content().toString(StandardCharsets.UTF_8).trim();
    log.info("Received a string '{}'", inputStr);
    return Mono.just(inputStr)
        .map(sensorInputParser::parse)
        .flatMap(
            sensorInput -> eventPublisher.send(
                appConfig.topics().sensorInputsTopic(),
                sensorInputEventMapper.map(sensorInput, sensorType, Instant.now())
            )
        )
        .map(senderResult -> "OK")
        .onErrorResume(
            SensorInputParsingException.class,
            e -> Mono.just("Rejected. %s".formatted(e.getMessage()))
        )
        .onErrorResume(
            Exception.class,
            e -> {
              log.error("Unexpected server error", e);
              return Mono.just("Server error, check the logs");
            }
        );
  }
}
