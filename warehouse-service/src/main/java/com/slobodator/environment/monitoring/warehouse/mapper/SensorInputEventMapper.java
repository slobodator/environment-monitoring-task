package com.slobodator.environment.monitoring.warehouse.mapper;

import com.slobodator.environment.monitoring.common.event.SensorInputEvent;
import com.slobodator.environment.monitoring.common.model.SensorType;
import com.slobodator.environment.monitoring.warehouse.model.SensorInput;
import java.time.Instant;
import org.mapstruct.Mapper;

@Mapper
public interface SensorInputEventMapper {
  SensorInputEvent map(SensorInput sensorInput, SensorType sensorType, Instant timestamp);
}
