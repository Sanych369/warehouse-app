package ru.damrin.app.common.handler;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.common.response.Response;

@Slf4j
@ControllerAdvice
public class Advice {

  @ExceptionHandler(WarehouseAppException.class)
  public ResponseEntity<Response> handleWarehouseAppException(final WarehouseAppException e) {
    return new ResponseEntity<>(
        Response.builder()
            .message(e.getMessage())
            .build(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Response> handleValidationException(final Exception e) {
    log.error("Unexpected exception {}. Cause: {}", e.getMessage(), e.getStackTrace());
    return new ResponseEntity<>(
        Response.builder().message(e.getMessage())
            .description(e.getMessage())
            .build(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
