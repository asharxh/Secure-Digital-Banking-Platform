package com.ashar.securedigitalbankingplatform.controller;

import com.ashar.securedigitalbankingplatform.entity.User;
import com.ashar.securedigitalbankingplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }
}
