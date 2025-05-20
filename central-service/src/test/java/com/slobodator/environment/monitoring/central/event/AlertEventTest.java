package com.slobodator.environment.monitoring.central.event;

import static com.slobodator.environment.monitoring.common.model.SensorType.TEMPERATURE;
import static com.slobodator.environment.monitoring.common.model.Severity.WARNING;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class AlertEventTest {
  @Test
  void key() {
    var sensorAlertEvent = new SensorAlertEvent(Instant.now(), "sensorId", TEMPERATURE, TEN, ONE, WARNING);
    assertThat(sensorAlertEvent.key())
        .isEqualTo("sensorId:TEMPERATURE");
  }
}