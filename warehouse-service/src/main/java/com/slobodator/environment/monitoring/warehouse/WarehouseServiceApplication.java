package com.slobodator.environment.monitoring.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class WarehouseServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(WarehouseServiceApplication.class, args);
  }
}
