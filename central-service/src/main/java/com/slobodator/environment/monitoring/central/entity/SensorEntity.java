package com.slobodator.environment.monitoring.central.entity;

import com.slobodator.environment.monitoring.common.model.SensorType;
import com.slobodator.environment.monitoring.common.model.Severity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "sensors")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PACKAGE)
public class SensorEntity {
  @SuppressWarnings("unused")
  @Id
  @GeneratedValue(generator = "sensors_id_seq")
  @SequenceGenerator(sequenceName = "sensors_id_seq", name = "sensors_id_seq")
  Integer id;

  String externalId;

  @Enumerated(EnumType.STRING)
  SensorType sensorType;

  @SuppressWarnings("FieldMayBeFinal")
  @ManyToMany(
      cascade = {
          CascadeType.PERSIST,
          CascadeType.REMOVE // thresholds will be removed once the sensor is gone
      }
  )
  @JoinTable(
      name = "sensor_thresholds",
      joinColumns = @JoinColumn(name = "sensor_id"),
      inverseJoinColumns = @JoinColumn(name = "threshold_id")
  )
  Set<ThresholdEntity> thresholds = new HashSet<>();

  @SuppressWarnings("unused")
  @Embedded
  AuditData auditData;

  public SensorEntity(
      @NonNull String externalId,
      @NonNull SensorType sensorType
  ) {
    this.externalId = externalId;
    this.sensorType = sensorType;
  }

  public SensorEntity addThreshold(BigDecimal value, Severity severity) {
    var threshold = new ThresholdEntity(value, severity, this);
    this.thresholds.add(threshold);
    return this;
  }
}
