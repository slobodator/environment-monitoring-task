package com.slobodator.environment.monitoring.central.event;

import com.slobodator.environment.monitoring.common.event.Event;
import com.slobodator.environment.monitoring.common.model.SensorType;
import java.math.BigDecimal;
import java.time.Instant;

public record SensorAlertEvent(
    Instant timestamp,
    String sensorId,
    SensorType sensorType,
    BigDecimal threshold,
    BigDecimal value
) implements Event {
  @Override
  public String key() {
    return "%s:%s".formatted(sensorId, sensorType);
  }
}
