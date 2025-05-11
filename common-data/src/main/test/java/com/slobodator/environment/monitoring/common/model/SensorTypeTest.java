package com.slobodator.environment.monitoring.common.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class SensorTypeTest {
  @Test
  void validEnum() {
    assertThat(SensorType.fromString("Temperature"))
        .isEqualTo(SensorType.TEMPERATURE);
  }

  @Test
  void invalidEnum() {
    assertThatThrownBy(
        () -> SensorType.fromString("invalid")
    ).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid SensorType 'invalid'");
  }
}