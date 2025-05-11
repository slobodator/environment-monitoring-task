package com.slobodator.environment.monitoring.central.handler;

import com.slobodator.environment.monitoring.central.config.AppConfig;
import com.slobodator.environment.monitoring.central.dao.SensorDao;
import com.slobodator.environment.monitoring.central.mapper.SensorAlertEventMapper;
import com.slobodator.environment.monitoring.central.publisher.EventPublisher;
import com.slobodator.environment.monitoring.common.event.SensorInputEvent;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class SensorInputEventHandler {
  AppConfig appConfig;
  SensorDao sensorDao;
  SensorAlertEventMapper sensorAlertEventMapper;
  EventPublisher eventPublisher;

  public void handle(SensorInputEvent event) {
    var sensor = sensorDao.findByExternalIdAndType(event.sensorId(), event.sensorType());

    sensor.checkThreshold(
        event.value(),
        () ->
            eventPublisher.send(
                appConfig.topics().sensorAlertsTopic(),
                sensorAlertEventMapper.map(sensor, event)
            )
    );
  }
}
