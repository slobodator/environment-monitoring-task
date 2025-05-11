package com.slobodator.environment.monitoring.central.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppConfig(
    Topics topics
) {

  public record Topics(
      String sensorInputsTopic,
      String sensorAlertsTopic
  ) {
  }
}
