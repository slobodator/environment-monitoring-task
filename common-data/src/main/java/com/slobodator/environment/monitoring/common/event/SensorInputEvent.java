package com.slobodator.environment.monitoring.common.event;

import com.slobodator.environment.monitoring.common.model.SensorType;
import java.math.BigDecimal;
import java.time.Instant;

public record SensorInputEvent(
    Instant timestamp,
    String sensorId,
    SensorType sensorType,
    BigDecimal value
) implements Event {
  @Override
  public String key() {
    return "%s:%s".formatted(sensorId, sensorType);
  }
}
