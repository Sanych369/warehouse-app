package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.damrin.app.model.UserDto;
import ru.damrin.app.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/add")
    public String addUser(@RequestBody UserDto userDto) {
        userService.saveUser(userDto);
        return "redirect:/admin/users";
    }

    @GetMapping("/getByEmail")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
        try {
            UserDto userDto = userService.getUserByEmail(email);
            return ResponseEntity.ok(userDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editUser(@RequestBody UserDto userDto) {
        try {
            userService.updateUser(userDto);
            return ResponseEntity.ok("Пользователь обновлен");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }
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
