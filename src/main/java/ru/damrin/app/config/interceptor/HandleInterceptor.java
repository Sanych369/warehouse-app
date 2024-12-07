package ru.damrin.app.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ru.damrin.app.util.MDCUtils;

import java.util.UUID;

/**
 * Трассировка header requestId
 */
@Component
@Slf4j
public class HandleInterceptor implements HandlerInterceptor {
  /**
   * Перехват запроса и проверка наличия requestId в заголовке.
   * Если requestId отсутствует - генерируем.
   *
   * @param request  текущий HTTP запрос
   * @param response текущий HTTP ответ
   * @param handler  хендлер
   * @return значение true/false по итогу исполнения предобработки
   * @throws Exception если метод отработает с ошибкой согласно контракту {@link HandlerInterceptor}
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String header = request.getHeader("requestId");
    if (StringUtils.hasText(header)) {
      MDCUtils.putRequestId(request.getHeader("requestId"));
    } else {
      MDCUtils.putRequestId(UUID.randomUUID().toString());
    }
    log.info("Intercepted method [{}] for URI {}", request.getMethod(), request.getRequestURI());
    return HandlerInterceptor.super.preHandle(request, response, handler);
  }

  /**
   * Пробрасываем заголовок requestId в ответ
   *
   * @param request      текущий HTTP запрос
   * @param response     текущий HTTP ответ
   * @param handler      хендлер
   * @param modelAndView {@code ModelAndView} результат работы хендлера
   *                     (может быть {@code null})
   * @throws Exception если метод отработает с ошибкой согласно контракту {@link HandlerInterceptor}
   */
  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    response.addHeader("requestId", MDCUtils.getRequestId());
    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
  }

  /**
   * Очищаем заголовок
   *
   * @param request  текущий HTTP запрос
   * @param response текущий HTTP ответ
   * @param handler  хендлер
   * @throws Exception если метод отработает с ошибкой согласно контракту {@link HandlerInterceptor}
   */
  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    MDCUtils.clearRequestId();
    HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
  }
}
