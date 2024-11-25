package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.damrin.app.model.LoginRequest;
import ru.damrin.app.service.LoginService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final LoginService authService;

    @GetMapping()
    public String showLoginPage() {
        return "login";
    }

    @PostMapping()
    public ResponseEntity<String> authenticate(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}