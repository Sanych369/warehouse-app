package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.damrin.app.model.UserDto;
import ru.damrin.app.service.UserService;

import java.util.List;

/**
 * Контроллер для управления пользователями.
 */
@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/page")
  public ResponseEntity<Page<UserDto>> getUsersPage(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "20") int size,
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "surname", required = false) String surname,
      @RequestParam(value = "position", required = false) String position,
      @RequestParam(value = "email", required = false) String email,
      @RequestParam(value = "sort", required = false) String sort) {

    final var usersPage = userService.getUsersPage(page, size, name, surname, position, email, sort);
    return ResponseEntity.ok(usersPage);
  }

  @PostMapping("/add")
  public String addUser(@RequestBody UserDto userDto) {
    userService.saveUser(userDto);
    return "redirect:/admin/users";
  }

  @GetMapping("/getByEmail")
  public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
    final var userDto = userService.getUserByEmail(email);
    return ResponseEntity.ok(userDto);
  }

  @PutMapping("/edit")
  public ResponseEntity<String> editUser(@RequestBody UserDto userDto) {
    userService.updateUser(userDto);
    return ResponseEntity.ok("Пользователь обновлен");
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> deleteUserByEmail(@RequestParam String email) {
    userService.deleteUserByEmail(email);
    return ResponseEntity.ok("Пользователь удален");
  }


  @GetMapping("/list")
  @ResponseBody
  public List<UserDto> getUsersList() {
    return userService.getAllUsers();
  }
}
