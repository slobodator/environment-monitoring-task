package com.slobodator.environment.monitoring.central;

import com.redis.testcontainers.RedisContainer;
import com.slobodator.environment.monitoring.central.config.AppConfig;
import com.slobodator.environment.monitoring.central.publisher.EventPublisher;
import com.slobodator.environment.monitoring.central.repository.SensorRepository;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.kafka.ConfluentKafkaContainer;

@SuppressWarnings("resource")
@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
public abstract class CentralServiceApplicationBaseTest {
  static ConfluentKafkaContainer confluentKafkaContainer = new ConfluentKafkaContainer("confluentinc/cp-kafka:7.8.1");

  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17")
      .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(10)));

  static RedisContainer redisContainer = new RedisContainer("redis:8");

  private static boolean testContainersDisabled() {
    return Boolean.getBoolean("testcontainers.disabled");
  }

  static {
    if (testContainersDisabled()) {
      log.info("TestContainers are disabled");
    } else {
      log.info("Starting TestContainers");
      confluentKafkaContainer.start();
      postgreSQLContainer.start();
      redisContainer.start();
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

      registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
      registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
      registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
      registry.add("spring.datasource.driver-class-name", postgreSQLContainer::getDriverClassName);

      registry.add("spring.data.redis.host", redisContainer::getRedisHost);
      registry.add("spring.data.redis.port", redisContainer::getRedisPort);
    }
  }

  @Autowired
  protected AppConfig appConfig;

  @Autowired
  protected EventPublisher eventPublisher;

  @Autowired
  protected SensorRepository sensorRepository;

  @Autowired
  protected CacheManager cacheManager;

  @BeforeEach
  void cleanUp() {
    sensorRepository.deleteNotPredefined();
  }
}
