package ru.damrin.app.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WarehouseAppException extends RuntimeException {

  private String message;
  private String description;

  public WarehouseAppException(String message) {
    this.message = message;
    this.description = null;
  }

}
