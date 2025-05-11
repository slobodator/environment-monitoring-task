package com.slobodator.environment.monitoring.central.mapper;

import com.slobodator.environment.monitoring.central.event.SensorAlertEvent;
import com.slobodator.environment.monitoring.central.model.Sensor;
import com.slobodator.environment.monitoring.common.event.SensorInputEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SensorAlertEventMapper {
  @Mapping(target = "sensorId", source = "sensor.sensorId")
  @Mapping(target = "sensorType", source = "sensor.sensorType")
  SensorAlertEvent map(Sensor sensor, SensorInputEvent event);
}
