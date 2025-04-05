package com.kyc.onboarding.controller;

import com.kyc.onboarding.model.User;
import com.kyc.onboarding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> loginRequest) {
        String token = userService.loginUser(loginRequest.get("email"), loginRequest.get("password"));
        if (token == null) {
            return Map.of("error", "Invalid Credentials");
        }
        
        String role = "CUSTOMER"; // Default role
        Optional<User> userOpt = userService.userRepository.findByEmail(loginRequest.get("email"));
        if (userOpt.isPresent()) {
            role = userOpt.get().getRole();
        }

        return Map.of("token", token, "role", role); // Sends the correct role dynamically
    }
}
