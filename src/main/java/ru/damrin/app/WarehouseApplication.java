package ru.damrin.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Точка входа spring-boot приложения.
 */
@EnableScheduling
@SpringBootApplication
public class WarehouseApplication {

  public static void main(String[] args) {
    SpringApplication.run(WarehouseApplication.class, args);
  }
}
