package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.damrin.app.db.entity.UserEntity;
import ru.damrin.app.model.UserDto;
import ru.damrin.app.service.UserService;

/**
 * Контроллер для панели информации о пользователе.
 */
@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

  private final UserService userService;

  @GetMapping()
  public String getProfile() {
    return "profile";
  }

  @GetMapping("/data")
  @ResponseBody
  public UserDto getProfileData(@AuthenticationPrincipal UserEntity user) {
    return userService.getProfile(user);
  }
}