package com.slobodator.environment.monitoring.warehouse.model;

import java.math.BigDecimal;

public record SensorInput(
    String sensorId,
    BigDecimal value
) {
}
