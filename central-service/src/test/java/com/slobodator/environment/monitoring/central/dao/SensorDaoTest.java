package com.slobodator.environment.monitoring.central.dao;

import static com.slobodator.environment.monitoring.common.model.SensorType.TEMPERATURE;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.slobodator.environment.monitoring.central.CentralServiceApplicationBaseTest;
import com.slobodator.environment.monitoring.central.entity.SensorEntity;
import com.slobodator.environment.monitoring.central.exception.SensorNotFoundException;
import com.slobodator.environment.monitoring.central.model.Sensor;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("DataFlowIssue")
class SensorDaoTest extends CentralServiceApplicationBaseTest {
  @Autowired
  private SensorDao sensorDao;

  @Test
  void findExistingSensor() {
    sensorRepository.save(
        new SensorEntity("sensorId", TEMPERATURE, ZERO)
    );

    assertThat(
        sensorDao.findByExternalIdAndType("sensorId", TEMPERATURE)
    ).isEqualTo(
        new Sensor("sensorId", TEMPERATURE, new BigDecimal("0.00"))
    );

    assertThat(
        cacheManager.getCache("central-service-sensors").get("sensorId:TEMPERATURE")
    ).isNotNull();
  }

  @Test
  void failsOnNonexistingSensor() {
    assertThatThrownBy(
        () -> sensorDao.findByExternalIdAndType("invalidId", TEMPERATURE)
    )
        .isInstanceOf(SensorNotFoundException.class)
        .hasMessage("There is no sensor with id 'invalidId' and type 'TEMPERATURE'");
  }
}