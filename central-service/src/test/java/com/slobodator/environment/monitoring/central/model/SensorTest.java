package com.slobodator.environment.monitoring.central.model;

import static com.slobodator.environment.monitoring.common.model.SensorType.TEMPERATURE;
import static com.slobodator.environment.monitoring.common.model.Severity.CRITICAL;
import static com.slobodator.environment.monitoring.common.model.Severity.WARNING;
import static java.math.BigDecimal.ZERO;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Set;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SensorTest {
  final Threshold warningThreshold = new Threshold("threshold", new BigDecimal(80), WARNING);
  final Threshold critialThreshold = new Threshold("threshold", new BigDecimal(100), CRITICAL);
  final Sensor sensor = new Sensor(
      "sensorId",
      TEMPERATURE,
      Set.of(
          warningThreshold,
          critialThreshold
      )
  );

  @Mock
  Consumer<Threshold> action;

  @Test
  void callingActionWarningIfThresholdIsExceed() {
    sensor.checkThreshold(new BigDecimal(90), action);
    verify(action).accept(warningThreshold);
  }

  @Test
  void callingActionCriticalIfThresholdIsExceed() {
    sensor.checkThreshold(new BigDecimal(110), action);
    verify(action).accept(critialThreshold);
  }

  @Test
  void callingActionOkIfThresholdIsNotExceed() {
    sensor.checkThreshold(ZERO, action);
    verify(action).accept(Threshold.OK);
  }
}