package ru.damrin.app.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер для аутентификации пользователей.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

  @GetMapping
  public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
    if (StringUtils.isNotEmpty(error)) {
      model.addAttribute("errorMessage", "Неверное имя пользователя или пароль.");
    }

    return "login";
  }
}