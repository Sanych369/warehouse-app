package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import ru.damrin.app.model.UserDto;
import ru.damrin.app.service.UserService;

@Controller
@RequiredArgsConstructor
public class ProfileController {


    private final UserService userService;

    @GetMapping("/profile")
    public UserDto getProfile() {
        return userService.getProfile();
    }


}

