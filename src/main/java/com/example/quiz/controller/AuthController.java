package com.example.quiz.controller;

import com.example.quiz.dto.AuthResponse;
import com.example.quiz.dto.LoginRequest;
import com.example.quiz.dto.RegisterRequest;
import com.example.quiz.dto.UserDto;
import com.example.quiz.model.User;
import com.example.quiz.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/api/auth/me")
    public ResponseEntity<UserDto> getMe() {
        return ResponseEntity.ok(authService.getCurrentUserProfile());
    }

    // Backward-compatible alias for existing frontend/test scripts
    @PostMapping("/api/register")
    public ResponseEntity<?> legacyRegister(@RequestBody Map<String, String> data) {
        RegisterRequest req = new RegisterRequest();
        req.setName(data.get("name"));
        req.setEmail(data.get("email"));
        req.setPassword(data.get("password"));
        req.setConfirmPassword(data.getOrDefault("confirmPassword", data.get("password")));
        AuthResponse res = authService.register(req);
        return ResponseEntity.ok(Map.of("id", res.getId(), "name", res.getName(), "email", res.getEmail(), "role", res.getRole(), "token", res.getToken()));
    }

    // Backward-compatible alias for existing frontend/test scripts
    @PostMapping("/api/login")
    public ResponseEntity<?> legacyLogin(@RequestBody Map<String, String> data) {
        LoginRequest req = new LoginRequest(data.get("email"), data.get("password"));
        AuthResponse res = authService.login(req);
        return ResponseEntity.ok(Map.of("id", res.getId(), "name", res.getName(), "email", res.getEmail(), "role", res.getRole(), "token", res.getToken()));
    }
}
