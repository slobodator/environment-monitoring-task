package com.slobodator.environment.monitoring.central.mapper;

import com.slobodator.environment.monitoring.central.entity.ThresholdEntity;
import com.slobodator.environment.monitoring.central.model.Threshold;
import org.mapstruct.Mapper;

@Mapper
public interface ThresholdMapper {
  Threshold map(ThresholdEntity thresholdEntity);
}
