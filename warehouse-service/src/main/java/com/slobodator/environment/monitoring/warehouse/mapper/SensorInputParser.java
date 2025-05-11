package com.slobodator.environment.monitoring.warehouse.mapper;

import com.slobodator.environment.monitoring.warehouse.model.SensorInput;
import java.math.BigDecimal;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SensorInputParser {
  static Pattern PATTERN = Pattern.compile("sensor_id=(\\w+); value=(.+)");

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public SensorInput parse(String str) {
    try {
      var matcher = PATTERN.matcher(str);
      matcher.matches();
      var sensorInput = new SensorInput(matcher.group(1), new BigDecimal(matcher.group(2)));
      log.info("Parsed successfully to '{}'", sensorInput);
      return sensorInput;
    } catch (Exception e) {
      log.error("Can't parse '{}'", str);
      throw new SensorInputParsingException("Can't parse '%s'".formatted(str), e);
    }
  }
}
