package com.slobodator.environment.monitoring.central.handler;

import static com.slobodator.environment.monitoring.common.model.SensorType.TEMPERATURE;
import static com.slobodator.environment.monitoring.common.model.Severity.CRITICAL;
import static com.slobodator.environment.monitoring.common.model.Severity.WARNING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import com.slobodator.environment.monitoring.central.CentralServiceApplicationBaseTest;
import com.slobodator.environment.monitoring.central.entity.SensorEntity;
import com.slobodator.environment.monitoring.common.event.SensorInputEvent;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.system.CapturedOutput;

class SensorInputEventHandlerTest extends CentralServiceApplicationBaseTest {
  @Test
  void sensorInputExceedsThreshold(CapturedOutput output) {
    var sensorEntity = new SensorEntity("sensorId", TEMPERATURE)
        .addThreshold(new BigDecimal(80), WARNING)
        .addThreshold(new BigDecimal(100), CRITICAL);
    sensorRepository.save(sensorEntity);

    IntStream.of(90, 110, 50)
        .forEach(
            i -> eventPublisher.send(
                appConfig.topics().sensorInputsTopic(),
                new SensorInputEvent(Instant.now(), "sensorId", TEMPERATURE, new BigDecimal(i))
            )
        );

    await()
        .pollInterval(Duration.ofSeconds(1))
        .atMost(Duration.ofSeconds(10))
        .untilAsserted(
            () -> assertThat(output.getOut())
                .contains(
                    "WARNING! Sensor 'sensorId' of type 'TEMPERATURE' threshold '80.00' is exceed with value '90'",
                    "CRITICAL! Sensor 'sensorId' of type 'TEMPERATURE' threshold '100.00' is exceed with value '110'",
                    "OK! Sensor 'sensorId' of type 'TEMPERATURE' value '50'"
                )
        );
  }
}