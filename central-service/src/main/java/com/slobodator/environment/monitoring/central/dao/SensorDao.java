package com.slobodator.environment.monitoring.central.dao;

import com.slobodator.environment.monitoring.central.exception.SensorNotFoundException;
import com.slobodator.environment.monitoring.central.mapper.SensorMapper;
import com.slobodator.environment.monitoring.central.model.Sensor;
import com.slobodator.environment.monitoring.central.repository.SensorRepository;
import com.slobodator.environment.monitoring.common.model.SensorType;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class SensorDao {
  SensorRepository sensorRepository;
  SensorMapper sensorMapper;

  @Cacheable(value = "central-service-sensors", key = "#externalId + ':' + #sensorType")
  @Transactional(readOnly = true)
  public Sensor findByExternalIdAndType(String externalId, SensorType sensorType) {
    var sensorEntity = sensorRepository.findByExternalIdAndSensorType(
        externalId, sensorType
    ).orElseThrow(
        () -> {
          log.error(
              "There is no sensor with id '{}' and type '{}'",
              externalId, sensorType
          );
          return new SensorNotFoundException(
              "There is no sensor with id '%s' and type '%s'"
                  .formatted(externalId, sensorType)
          );
        }
    );
    return sensorMapper.map(sensorEntity);
  }
}
