package com.slobodator.environment.monitoring.central.entity;

import jakarta.persistence.Embeddable;
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Embeddable
public class AuditData {
  @SuppressWarnings("unused")
  @CreationTimestamp
  protected Instant createdAt;

  @SuppressWarnings("unused")
  @UpdateTimestamp
  protected Instant modifiedAt;
}
