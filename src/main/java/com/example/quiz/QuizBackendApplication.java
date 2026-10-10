package com.example.quiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
@RestController
public class QuizBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuizBackendApplication.class, args);
    }

    @GetMapping("/api/health")
    public Map<String, Object> home() {
        return Map.of(
            "status", "UP",
            "message", "Online Quiz Management System Backend is running!",
            "endpoints", Map.of(
                "quizzes", "/api/quizzes",
                "login", "/api/auth/login",
                "register", "/api/auth/register",
                "leaderboard", "/api/leaderboard",
                "categories", "/api/categories"
            )
        );
    }
}
