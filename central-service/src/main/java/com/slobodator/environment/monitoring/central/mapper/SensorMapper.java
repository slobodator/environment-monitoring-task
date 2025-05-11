package com.slobodator.environment.monitoring.central.mapper;

import com.slobodator.environment.monitoring.central.entity.SensorEntity;
import com.slobodator.environment.monitoring.central.model.Sensor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SensorMapper {
  @Mapping(target = "sensorId", source = "externalId")
  Sensor map(SensorEntity sensor);
}
