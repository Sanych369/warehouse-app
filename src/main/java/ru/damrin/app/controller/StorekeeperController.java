package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/storekeeper")
@RequiredArgsConstructor
public class StorekeeperController {

  @GetMapping
  public String showDashboard() {
    return "storekeeper";
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
