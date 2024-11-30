package ru.damrin.app.common.validation;


import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationService<T> {

  private final Validator validator;


  public T validate(T object) {
    var violations = validator.validate(object);

    if (!violations.isEmpty()) {
      var message = violations.stream()
          .map(x -> x.getPropertyPath() + x.getMessage())
          .collect(Collectors.joining(", "));
      log.error("Validation errors: {}", message);
      throw new ValidationException(message);
    }
    return object;
  }
}
