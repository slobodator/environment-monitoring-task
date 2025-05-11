package com.slobodator.environment.monitoring.warehouse;

import com.slobodator.environment.monitoring.warehouse.config.AppConfig;
import com.slobodator.environment.monitoring.warehouse.util.TestListener;
import java.io.IOException;
import java.net.DatagramSocket;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.kafka.ConfluentKafkaContainer;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
public abstract class WarehouseServiceApplicationBaseTest {
  static ConfluentKafkaContainer confluentKafkaContainer = new ConfluentKafkaContainer("confluentinc/cp-kafka:7.8.1");

  private static boolean testContainersDisabled() {
    return Boolean.getBoolean("testcontainers.disabled");
  }

  private static int findFreeUdpPort() {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    } catch (IOException e) {
      throw new IllegalStateException("Could not find a free UDP port", e);
    }
  }

  static {
    if (testContainersDisabled()) {
      log.info("TestContainers are disabled");
    } else {
      log.info("Starting TestContainers");
      confluentKafkaContainer.start();
    }
  }

  @SuppressWarnings("unused")
  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry registry) {
    if (testContainersDisabled()) {
      log.info("Dynamic properties are not set");
    } else {
      log.info("Dynamic properties are amended for TestContainers");
      registry.add("spring.kafka.bootstrap-servers", confluentKafkaContainer::getBootstrapServers);
    }
    registry.add("app.udp-port", WarehouseServiceApplicationBaseTest::findFreeUdpPort);
  }

  @Autowired
  protected AppConfig appConfig;

  @Autowired
  protected TestListener testListener;


  @BeforeEach
  void commonSetUp() {
    testListener.reset();
  }
}
