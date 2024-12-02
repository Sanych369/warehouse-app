package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping
    public String showDashboard() {
        return "admin-dashboard";  // Панель администратора
    }

    @GetMapping("/users")
    public String showUsersPage(Model model) {
        // Получаем список пользователей и передаем в модель
        return "users";  // Переход к странице списка пользователей
    }
//    @GetMapping("/goods")
//    public String showGoodsPage(Model model) {
//        // Получаем список пользователей и передаем в модель
//        return "goods";  // Переход к странице списка пользователей
//    }

    @GetMapping("/goods")
    public String showGoodsPage(Model model) {
        // Получаем список пользователей и передаем в модель
        return "goods";  // Переход к странице списка пользователей
    }

    @GetMapping("/companies")
    public String showCompaniesPage(Model model) {
        // Получаем список пользователей и передаем в модель
        return "companies";  // Переход к странице списка пользователей
    }

    @GetMapping("/categories")
    public String showCategoryPage(Model model) {
        // Получаем список пользователей и передаем в модель
        return "categories";  // Переход к странице списка пользователей
    }
}