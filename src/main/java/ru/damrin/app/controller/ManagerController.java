package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

  @GetMapping
  public String showDashboard() {
    return "manager";
  }

  @GetMapping("/orders")
  public String showOrdersPage() {
    return "orders-my";
  }

  @GetMapping("/companies")
  public String showCompaniesPage() {
    return "companies";
  }

  @GetMapping("/reports")
  public String showReportsPage() {
    return "reports";
  }
}
