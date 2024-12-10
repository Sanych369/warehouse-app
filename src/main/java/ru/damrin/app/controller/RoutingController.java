package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.damrin.app.common.enums.Position;

import static java.util.Objects.nonNull;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class RoutingController {

  @GetMapping
  public String redirectToDashboard(Authentication authentication) {
    if (nonNull(authentication)) {
      if (authentication.getAuthorities().stream()
          .anyMatch(role -> role.getAuthority().equals(Position.ADMIN.name()))) {
        return "redirect:/admin";
      }
      if (authentication.getAuthorities().stream()
          .anyMatch(role -> role.getAuthority().equals(Position.MANAGER.name()))) {
        return "redirect:/manager";
      }
      if (authentication.getAuthorities().stream()
          .anyMatch(role -> role.getAuthority().equals(Position.STOREKEEPER.name()))) {
        return "redirect:/storekeeper";
      }
    }
    return "redirect:/login";
  }

  @GetMapping("/help")
  public String getHelpPage() {
    return "help";
  }
}
