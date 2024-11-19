package ru.damrin.app.common.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.common.response.Response;

@ControllerAdvice
public class Advice {

  @ExceptionHandler(WarehouseAppException.class)
  public ResponseEntity<Response> handleWarehouseAppException(final WarehouseAppException e) {
    return new ResponseEntity<>(
        Response.builder()
            .message(e.getMessage())
            .description(e.getDescription())
            .build(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
