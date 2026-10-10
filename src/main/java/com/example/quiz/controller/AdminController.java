package com.example.quiz.controller;

import com.example.quiz.dto.*;
import com.example.quiz.model.Quiz;
import com.example.quiz.service.AdminService;
import com.example.quiz.service.QuestionService;
import com.example.quiz.service.QuizService;
import com.example.quiz.service.ResultService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final QuizService quizService;
    private final QuestionService questionService;
    private final AdminService adminService;
    private final ResultService resultService;

    public AdminController(QuizService quizService,
                           QuestionService questionService,
                           AdminService adminService,
                           ResultService resultService) {
        this.quizService = quizService;
        this.questionService = questionService;
        this.adminService = adminService;
        this.resultService = resultService;
    }

    // --- Dashboard & Analytics ---
    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDto> getDashboard() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/analytics")
    public ResponseEntity<AdminDashboardDto> getAnalytics() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    // --- Students Management ---
    @GetMapping("/students")
    public ResponseEntity<List<UserDto>> getStudents(@RequestParam(required = false) String search) {
        return ResponseEntity.ok(adminService.getAllStudents(search));
    }

    // --- Results Management ---
    @GetMapping("/results")
    public ResponseEntity<List<StudentResultDto>> getResults(
            @RequestParam(required = false) Long quizId,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(resultService.getAllResultsForAdmin(quizId, search));
    }

    // --- Quiz CRUD ---
    @GetMapping("/quizzes")
    public ResponseEntity<List<QuizDto>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllQuizzesForAdmin());
    }

    @PostMapping("/quizzes")
    public ResponseEntity<QuizDto> createQuiz(@RequestBody Quiz quiz) {
        return ResponseEntity.ok(quizService.createQuiz(quiz));
    }

    @PutMapping("/quizzes/{id}")
    public ResponseEntity<QuizDto> updateQuiz(@PathVariable Long id, @RequestBody Quiz quiz) {
        return ResponseEntity.ok(quizService.updateQuiz(id, quiz));
    }

    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/quizzes/{id}/publish")
    public ResponseEntity<QuizDto> togglePublish(@PathVariable Long id, @RequestBody(required = false) Map<String, Boolean> body) {
        Boolean published = body != null ? body.get("published") : null;
        return ResponseEntity.ok(quizService.togglePublish(id, published));
    }

    // --- Questions CRUD ---
    @GetMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<List<QuestionAdminDto>> getQuestions(@PathVariable Long quizId) {
        return ResponseEntity.ok(questionService.getQuestionsForAdmin(quizId));
    }

    @PostMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<QuestionAdminDto> addQuestion(
            @PathVariable Long quizId,
            @Valid @RequestBody QuestionAdminDto dto) {
        return ResponseEntity.ok(questionService.addQuestion(quizId, dto));
    }

    @PutMapping("/questions/{id}")
    public ResponseEntity<QuestionAdminDto> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody QuestionAdminDto dto) {
        return ResponseEntity.ok(questionService.updateQuestion(id, dto));
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}
