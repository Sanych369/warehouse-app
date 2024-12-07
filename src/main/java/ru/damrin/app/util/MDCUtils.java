package ru.damrin.app.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.slf4j.MDC;
import ru.damrin.app.config.interceptor.HandleInterceptor;

@UtilityClass
public class MDCUtils {
  /**
   * Наименование параметра для трассировки
   */
  private final String REQUEST_ID = "requestId";

  /**
   * Получение значения requestId из контекста
   *
   * @return requestId
   */
  public String getRequestId() {
    return MDC.get(REQUEST_ID);
  }

  /**
   * Сохранение в контекст значения requestId
   *
   * @param requestId значение полученное либо из header'а запроса, либо сгенерированное
   *                  {@link HandleInterceptor#preHandle(HttpServletRequest, HttpServletResponse, Object)}
   */
  public void putRequestId(String requestId) {
    MDC.put(REQUEST_ID, requestId);
  }

  /**
   * Очистка текущего requestId из контекста
   */
  public void clearRequestId() {
    MDC.remove(REQUEST_ID);
  }
}
