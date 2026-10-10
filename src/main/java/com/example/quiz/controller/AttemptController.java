package com.example.quiz.controller;

import com.example.quiz.dto.AttemptResultDto;
import com.example.quiz.dto.StartAttemptDto;
import com.example.quiz.dto.SubmitAttemptRequest;
import com.example.quiz.model.User;
import com.example.quiz.service.AttemptService;
import com.example.quiz.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attempts")
@CrossOrigin(origins = "*")
public class AttemptController {

    private final AttemptService attemptService;
    private final AuthService authService;

    public AttemptController(AttemptService attemptService, AuthService authService) {
        this.attemptService = attemptService;
        this.authService = authService;
    }

    @PostMapping("/start/{quizId}")
    public ResponseEntity<StartAttemptDto> startAttempt(@PathVariable Long quizId) {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(attemptService.startAttempt(quizId, user));
    }

    @GetMapping("/{attemptId}")
    public ResponseEntity<StartAttemptDto> getAttempt(@PathVariable Long attemptId) {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(attemptService.getAttemptStatus(attemptId, user));
    }

    @PostMapping("/{attemptId}/submit")
    public ResponseEntity<AttemptResultDto> submitAttempt(
            @PathVariable Long attemptId,
            @RequestBody SubmitAttemptRequest request) {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(attemptService.submitAttempt(attemptId, request, user));
    }
}
