package com.example.quiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
                "login", "/api/login",
                "register", "/api/register"
            )
        );
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
