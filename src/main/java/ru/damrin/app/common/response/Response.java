package ru.damrin.app.common.response;

import lombok.Builder;
import lombok.Data;

/**
 * Кастомный ответ при получении исключений.
 */
@Data
@Builder
public class Response {

  private String message;
  private String description;
}
