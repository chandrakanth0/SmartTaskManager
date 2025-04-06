package com.taskmanager.controller;

import com.taskmanager.entity.User;
import com.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder encoder;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        logger.info("Registering user: {}", user.getUsername());

        user.setPassword(encoder.encode(user.getPassword()));
        System.out.println("\n\n\n " + user + "\n\n\n");
        userRepo.save(user);

        logger.info("User registered successfully: {}", user.getUsername());
        return "User registered successfully";
    }
}
