package com.slobodator.environment.monitoring.central.model;

import com.slobodator.environment.monitoring.common.model.SensorType;
import jakarta.annotation.Nullable;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Sensor implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  String sensorId;
  SensorType sensorType;
  @Nullable
  BigDecimal threshold;

  public void checkThreshold(BigDecimal value, Runnable action) {
    if (threshold != null && value.compareTo(threshold) >= 0) {
      log.info(
          "Value '{}' exceeds the threshold '{}' for sensor '{}:{}', triggering the action",
          value, threshold, sensorId, sensorType
      );
      action.run();
    } else {
      log.debug(
          "Value '{}' doesn't exceed the threshold '{}' for sensor '{}:{}', no action is triggered",
          value, threshold, sensorId, sensorType
      );
    }
  }
}
