package com.slobodator.environment.monitoring.central.mapper;

import com.slobodator.environment.monitoring.central.event.SensorAlertEvent;
import com.slobodator.environment.monitoring.central.model.Sensor;
import com.slobodator.environment.monitoring.central.model.Threshold;
import com.slobodator.environment.monitoring.common.event.SensorInputEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SensorAlertEventMapper {
  @Mapping(target = "sensorId", source = "sensor.sensorId")
  @Mapping(target = "sensorType", source = "sensor.sensorType")
  @Mapping(target = "threshold", source = "threshold.value")
  @Mapping(target = "value", source = "event.value")
  SensorAlertEvent map(Sensor sensor, SensorInputEvent event, Threshold threshold);
}
