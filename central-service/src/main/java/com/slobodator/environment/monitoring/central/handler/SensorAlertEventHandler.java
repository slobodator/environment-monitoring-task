package com.slobodator.environment.monitoring.central.handler;

import com.slobodator.environment.monitoring.central.event.SensorAlertEvent;
import com.slobodator.environment.monitoring.common.model.Severity;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class SensorAlertEventHandler {
  public void handle(SensorAlertEvent event) {
    if (event.severity() != Severity.OK) {
      log.warn(
          "{}! Sensor '{}' of type '{}' threshold '{}' is exceed with value '{}' at '{}'",
          event.severity(), event.sensorId(), event.sensorType(), event.threshold(), event.value(), event.timestamp()
      );
    } else {
      log.info(
          "OK! Sensor '{}' of type '{}' value '{}' at '{}', which is ok",
          event.sensorId(), event.sensorType(), event.value(), event.timestamp()
      );
    }
  }
}
