package com.slobodator.environment.monitoring.common.model;

import java.util.stream.Stream;

public enum SensorType {
  TEMPERATURE,
  HUMIDITY;

  public static SensorType fromString(String str) {
    return Stream.of(SensorType.values())
        .filter(v -> v.name().equalsIgnoreCase(str))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(
                "Invalid SensorType '%s'".formatted(str)
            )
        );
  }
}
