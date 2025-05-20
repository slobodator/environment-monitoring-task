package com.slobodator.environment.monitoring.central.entity;

import com.slobodator.environment.monitoring.common.model.SensorType;
import com.slobodator.environment.monitoring.common.model.Severity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Entity
@Table(name = "thresholds")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class ThresholdEntity {
  @SuppressWarnings("unused")
  @Id
  @GeneratedValue(generator = "thresholds_id_seq")
  @SequenceGenerator(sequenceName = "thresholds_id_seq", name = "thresholds_id_seq")
  Integer id;

  String name;

  @Enumerated(EnumType.STRING)
  SensorType sensorType;

  BigDecimal value;

  @Enumerated(EnumType.STRING)
  Severity severity;

  @SuppressWarnings("unused")
  @Embedded
  AuditData auditData;

  @SuppressWarnings("FieldMayBeFinal")
  @ManyToMany(mappedBy = "thresholds")
  Set<SensorEntity> sensors = new HashSet<>();

  public ThresholdEntity(
      @NonNull String name,
      @NonNull SensorType sensorType,
      @NonNull BigDecimal value,
      @NonNull Severity severity
  ) {
    this.name = name;
    this.sensorType = sensorType;
    this.value = value;
    this.severity = severity;
  }

  ThresholdEntity(BigDecimal value, Severity severity, SensorEntity sensorEntity) {
    this(
        sensorEntity.externalId + "_threshold",
        sensorEntity.sensorType,
        value,
        severity
    );
    this.sensors.add(sensorEntity);
  }
}
