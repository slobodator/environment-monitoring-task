package com.slobodator.environment.monitoring.central.dao;

import static com.slobodator.environment.monitoring.common.model.SensorType.TEMPERATURE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.slobodator.environment.monitoring.central.CentralServiceApplicationBaseTest;
import com.slobodator.environment.monitoring.central.entity.SensorEntity;
import com.slobodator.environment.monitoring.central.exception.SensorNotFoundException;
import com.slobodator.environment.monitoring.central.model.Sensor;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("DataFlowIssue")
class SensorDaoTest extends CentralServiceApplicationBaseTest {
  @SuppressWarnings("unused")
  @Autowired
  private SensorDao sensorDao;

  @Test
  void findExistingSensor() {
    sensorRepository.save(
        new SensorEntity("sensorId", TEMPERATURE)
    );

    assertThat(
        sensorDao.findByExternalIdAndType("sensorId", TEMPERATURE)
    ).isEqualTo(
        new Sensor("sensorId", TEMPERATURE, Collections.emptySet())
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