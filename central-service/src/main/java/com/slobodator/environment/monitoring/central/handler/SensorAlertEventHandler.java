package com.slobodator.environment.monitoring.central.handler;

import com.slobodator.environment.monitoring.central.event.SensorAlertEvent;
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
    log.warn(
        "ALARM! Sensor '{}' of type '{}' threshold '{}' is exceed with value '{}' at '{}'",
        event.sensorId(), event.sensorType(), event.threshold(), event.value(), event.timestamp()
    );
  }
}
