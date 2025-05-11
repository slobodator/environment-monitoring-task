package com.slobodator.environment.monitoring.central.repository;

import com.slobodator.environment.monitoring.central.entity.SensorEntity;
import com.slobodator.environment.monitoring.common.model.SensorType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SensorRepository extends JpaRepository<SensorEntity, Integer> {
  Optional<SensorEntity> findByExternalIdAndSensorType(String externalId, SensorType sensorType);

  @Transactional
  @Modifying
  @Query("delete from SensorEntity s where s.id > 0")
  void deleteNotPredefined();
}
