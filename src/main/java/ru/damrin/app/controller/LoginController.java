package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.damrin.app.model.LoginRequest;
import ru.damrin.app.service.LoginService;

/**
 * Контроллер для аутентификации пользователей.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

  private final LoginService authService;

  @GetMapping()
  public String showLoginPage() {
    return "login";
  }

  @PostMapping()
  public ResponseEntity<String> authenticate(@RequestBody LoginRequest request) {
    return ResponseEntity.ok(authService.authenticate(request));
  }
}