package com.slobodator.environment.monitoring.common.event;

import static com.slobodator.environment.monitoring.common.model.SensorType.TEMPERATURE;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class SensorInputEventTest {
  @Test
  void key() {
    var sensorInputEvent = new SensorInputEvent(Instant.now(), "sensorId", TEMPERATURE, TEN);
    assertThat(sensorInputEvent.key())
        .isEqualTo("sensorId:TEMPERATURE");
  }
}