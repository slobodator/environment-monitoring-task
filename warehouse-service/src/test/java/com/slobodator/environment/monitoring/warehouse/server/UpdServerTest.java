package com.slobodator.environment.monitoring.warehouse.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import com.slobodator.environment.monitoring.warehouse.WarehouseServiceApplicationBaseTest;
import com.slobodator.environment.monitoring.warehouse.model.SensorInput;
import com.slobodator.environment.monitoring.warehouse.util.UdpClient;
import java.math.BigDecimal;
import java.time.Duration;
import org.junit.jupiter.api.Test;

@SuppressWarnings("DataFlowIssue")
public class UpdServerTest extends WarehouseServiceApplicationBaseTest {
  @Test
  void success() throws Exception {
    var udpClient = new UdpClient(appConfig.udpPort());
    udpClient.sendMessage("sensor_id=t1; value=30");

    await()
        .pollInterval(Duration.ofSeconds(1))
        .atMost(Duration.ofSeconds(5))
        .untilAsserted(
            () -> {
              var events = testListener.getEvents(
                  appConfig.topics().sensorInputsTopic(),
                  SensorInput.class
              );
              assertAll(
                  () -> assertThat(events)
                      .hasSize(1),
                  () -> assertThat(events.getFirst())
                      .isEqualTo(new SensorInput("t1", new BigDecimal(30)))
              );
            }
        );
  }

  @Test
  void reject() throws Exception {
    var udpClient = new UdpClient(appConfig.udpPort());
    var received = udpClient.sendMessage("invalid");
    assertThat(received).isEqualTo("Rejected. Can't parse 'invalid'");
  }
}
