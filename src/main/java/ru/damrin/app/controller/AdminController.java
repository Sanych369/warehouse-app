package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер для обработки запросов панели Администратора.
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

  @GetMapping
  public String showDashboard() {
    return "admin";
  }

  @GetMapping("/orders")
  public String showOrdersPage() {
    return "orders";
  }

  @GetMapping("/users")
  public String showUsersPage() {
    return "users";
  }

  @GetMapping("/goods")
  public String showGoodsPage() {
    return "goods";
  }

  @GetMapping("/companies")
  public String showCompaniesPage() {
    return "companies";
  }

  @GetMapping("/store")
  public String showStorePage() {
    return "store";
  }

  @GetMapping("/reports")
  public String showReportsPage() {
    return "reports";
  }
}