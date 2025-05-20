package com.slobodator.environment.monitoring.warehouse.config;

import com.slobodator.environment.monitoring.common.model.SensorType;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@Slf4j
public record AppConfig(
    String udpPorts,
    Topics topics
) {
  public Map<SensorType, Integer> portsMapping() {
    try {
      return Stream.of(udpPorts.split(","))
          .map(s -> s.split(":"))
          .collect(
              Collectors.toMap(
                  k -> SensorType.fromString(k[0].trim()),
                  v -> Integer.parseInt(v[1].trim())
              )
          );
    } catch (Exception e) {
      log.error("Can't parse udpPorts '{}'", udpPorts);
      throw new IllegalArgumentException("Can't parse udpPorts '%s'".formatted(udpPorts));
    }
  }

  public record Topics(
      String sensorInputsTopic
  ) {
  }
}
