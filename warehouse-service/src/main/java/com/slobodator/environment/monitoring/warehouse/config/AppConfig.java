package com.slobodator.environment.monitoring.warehouse.config;

import com.slobodator.environment.monitoring.common.model.SensorType;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppConfig(
    int udpPort,
    SensorType sensorType,
    Topics topics
) {

  public record Topics(
      String sensorInputsTopic
  ) {
  }
}
