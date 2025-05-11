package com.slobodator.environment.monitoring.warehouse.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.slobodator.environment.monitoring.warehouse.model.SensorInput;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class SensorInputParserTest {
  final SensorInputParser parser = new SensorInputParser();

  @Test
  void validFormat() {
    assertThat(
        parser.parse("sensor_id=t1; value=30")
    ).isEqualTo(
        new SensorInput("t1", new BigDecimal(30))
    );
  }

  @Test
  void invalidFormat() {
    assertThatThrownBy(
        () -> parser.parse("invalid")
    )
        .isInstanceOf(SensorInputParsingException.class)
        .hasMessage("Can't parse 'invalid'");
  }
}