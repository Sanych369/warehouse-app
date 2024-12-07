package ru.damrin.app.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.common.response.Response;

/**
 * АОП advice для отлова исключений и выдачи стандартизированного ответа фронту.
 */
@Slf4j
@ControllerAdvice
public class Advice {

  /**
   * В случае отлова кастомного исключения - выдаём его мессадж наружу.
   *
   * @param e {@link WarehouseAppException} - кастомный ексепшен
   * @return {@link  Response} - ответ фронту в читабельном виде
   */
  @ExceptionHandler(WarehouseAppException.class)
  public ResponseEntity<Response> handleWarehouseAppException(final WarehouseAppException e) {
    return new ResponseEntity<>(
        Response.builder()
            .message(e.getMessage())
            .build(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Общий обработчик исключений, выводим текст, отправляемся в логи, фиксим.
   *
   * @param e - неожидаемое исключение
   * @return {@link  Response} - ответ фронту с мессаджем
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Response> handleValidationException(final Exception e) {
    log.error("Unexpected exception {}. Cause: {}", e.getMessage(), e.getStackTrace());
    return new ResponseEntity<>(
        Response.builder().message("Unexpected exception")
            .description(e.getMessage())
            .build(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
