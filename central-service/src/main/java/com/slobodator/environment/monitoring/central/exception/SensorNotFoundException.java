package com.slobodator.environment.monitoring.central.exception;

public class SensorNotFoundException extends RuntimeException {
  public SensorNotFoundException(String message) {
    super(message);
  }
}
