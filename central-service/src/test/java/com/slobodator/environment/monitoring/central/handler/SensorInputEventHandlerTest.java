package com.slobodator.environment.monitoring.central.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import com.slobodator.environment.monitoring.central.CentralServiceApplicationBaseTest;
import com.slobodator.environment.monitoring.central.entity.SensorEntity;
import com.slobodator.environment.monitoring.common.event.SensorInputEvent;
import com.slobodator.environment.monitoring.common.model.SensorType;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.system.CapturedOutput;

class SensorInputEventHandlerTest extends CentralServiceApplicationBaseTest {
  @Test
  void sensorInputExceedsThreshold(CapturedOutput output) {
    var sensor = new SensorEntity(
        "sensorId",
        SensorType.TEMPERATURE,
        BigDecimal.ZERO
    );
    sensorRepository.save(
        sensor
    );

    eventPublisher.send(
        appConfig.topics().sensorInputsTopic(),
        new SensorInputEvent(Instant.now(), "sensorId", SensorType.TEMPERATURE, BigDecimal.TEN)
    );

    await()
        .pollInterval(Duration.ofSeconds(1))
        .atMost(Duration.ofSeconds(30))
        .untilAsserted(
            () -> assertThat(output.getOut())
                .contains("""
                    ALARM! Sensor 'sensorId' of type 'TEMPERATURE'\
                     threshold '0.00' is exceed with value '10'\
                    """
                )
        );
  }
}