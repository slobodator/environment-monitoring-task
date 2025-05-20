package com.slobodator.environment.monitoring.warehouse.config;

import static com.slobodator.environment.monitoring.common.model.SensorType.HUMIDITY;
import static com.slobodator.environment.monitoring.common.model.SensorType.TEMPERATURE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import org.junit.jupiter.api.Test;

class AppConfigTest {
  @Test
  void portsMappingParsedOk() {
    var appConfig = new AppConfig(
        "temperature: 3344 , humidity :3355",
        new AppConfig.Topics("sensor-inputs-topic")
    );

    assertThat(
        appConfig.portsMapping()
    ).isEqualTo(
        Map.of(TEMPERATURE, 3344, HUMIDITY, 3355)
    );
  }

  @Test
  void portsMappingInvalidInput() {
    var appConfig = new AppConfig(
        "invalid",
        new AppConfig.Topics("sensor-inputs-topic")
    );

    assertThatThrownBy(
        appConfig::portsMapping
    )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Can't parse udpPorts 'invalid'");
  }
}