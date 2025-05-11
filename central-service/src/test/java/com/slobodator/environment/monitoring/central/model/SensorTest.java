package com.slobodator.environment.monitoring.central.model;

import static com.slobodator.environment.monitoring.common.model.SensorType.TEMPERATURE;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SensorTest {
  final Sensor sensor = new Sensor("sensorId", TEMPERATURE, ONE);

  @Mock
  Runnable action;

  @Test
  void callingActionIfThresholdIsExceed() {
    sensor.checkThreshold(TEN, action);
    verify(action).run();
  }

  @Test
  void noActionIfThresholdIsNotExceed() {
    sensor.checkThreshold(ZERO, action);
    verify(action, never()).run();
  }

  @Test
  void noActionIfThresholdIsNotSet() {
    var noThresholdSensor = new Sensor("sensorId", TEMPERATURE, null);
    noThresholdSensor.checkThreshold(ZERO, action);
    verify(action, never()).run();
  }
}