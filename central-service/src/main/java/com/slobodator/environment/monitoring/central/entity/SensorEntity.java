package com.slobodator.environment.monitoring.central.entity;

import com.slobodator.environment.monitoring.common.model.SensorType;
import jakarta.annotation.Nullable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Entity
@Table(name = "sensors")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class SensorEntity {
  @SuppressWarnings("unused")
  @Id
  @GeneratedValue(generator = "sensors_id_seq")
  @SequenceGenerator(sequenceName = "sensors_id_seq", name = "sensors_id_seq")
  Integer id;

  String externalId;

  @Enumerated(EnumType.STRING)
  SensorType sensorType;

  @Nullable
  BigDecimal threshold;

  @SuppressWarnings("unused")
  @Embedded
  AuditData auditData;

  public SensorEntity(
      @NonNull String externalId,
      @NonNull SensorType sensorType,
      @Nullable BigDecimal threshold
  ) {
    this.externalId = externalId;
    this.sensorType = sensorType;
    this.threshold = threshold;
  }
}
