package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/goods")
    public String showGoodsPage(Model model) {
        // Получаем список пользователей и передаем в модель
        return "goods";  // Переход к странице списка пользователей
    }



//
//    // API метод для получения всех пользователей в формате JSON
//    @GetMapping("/users/all")
//    @ResponseBody
//    public List<UserEntity> getAllUsers() {
//        return userService.getAllUsers();
////        model.addAttribute("users", userService.getAllUsers());
//
//    }
//
//
//
//    // Страница добавления пользователя
//    @GetMapping("/add")
//    public String showAddUserForm(Model model) {
//        model.addAttribute("user", new UserEntity());
//        return "add-user";  // Отображаем форму добавления пользователя
//    }
//
//    // Добавление пользователя
//    @PostMapping
//    public String addUser(@ModelAttribute UserEntity user) {
//        userService.createUser(user);
//        return "redirect:/admin/users";  // Перенаправляем на страницу с пользователями
//    }
//
//    // Страница редактирования пользователя
//    @GetMapping("/edit/{id}")
//    public String showEditUserForm(@PathVariable Long id, Model model) {
//        model.addAttribute("user", userService.getUserById(id));
//        return "edit-user";  // Отображаем форму редактирования
//    }
//
//    // Обновление пользователя
//    @PostMapping("/edit/{id}")
//    public String updateUser(@PathVariable Long id, @ModelAttribute UserEntity user) {
//        userService.updateUser(id, user);
//        return "redirect:/admin/users";  // Перенаправляем на страницу с пользователями
//    }
//
//    // Удаление пользователя
//    @PostMapping("/delete/{id}")
//    public String deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return "redirect:/admin/users";  // Перенаправляем на страницу с пользователями
//    }

//    // Поиск пользователей по запросу
//    @GetMapping("/search")
//    @ResponseBody
//    public List<UserEntity> searchUsers(@RequestParam String query) {
//        return userService.searchUsers(query);
//    }
}