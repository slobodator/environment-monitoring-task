package com.slobodator.environment.monitoring.central.model;

import com.slobodator.environment.monitoring.common.model.SensorType;
import com.slobodator.environment.monitoring.common.model.Severity;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record Sensor(
    String sensorId,
    SensorType sensorType,
    Set<Threshold> thresholds
) implements Serializable {
  @Serial
  private static final long serialVersionUID = 3512340661603093085L;

  public void checkThreshold(BigDecimal value, Consumer<Threshold> action) {
    thresholds.stream()
        .filter(t -> value.compareTo(t.value()) >= 0)
        .max(Comparator.comparing(Threshold::severity, Severity.IndexComparator.INSTANCE))
        .ifPresentOrElse(
            t -> {
              log.info(
                  "Value '{}' exceeds the threshold '{}:{}' for sensor '{}:{}', triggering the action",
                  value, t.name(), t.severity(), sensorId, sensorType
              );
              action.accept(t);
            },
            () -> {
              log.debug(
                  "Value '{}' doesn't exceed any threshold for sensor '{}:{}', triggering the OK action",
                  value, sensorId, sensorType
              );
              action.accept(Threshold.OK);
            }
        );
  }
}
