package com.slobodator.environment.monitoring.central.model;

import com.slobodator.environment.monitoring.common.model.Severity;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public record Threshold(
    String name,
    BigDecimal value,
    Severity severity
) implements Serializable {
  public static final Threshold OK = new Threshold(null, null, Severity.OK);

  @Serial
  private static final long serialVersionUID = -8267024518076738342L;
}
