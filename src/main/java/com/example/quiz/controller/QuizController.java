package com.example.quiz.controller;

import com.example.quiz.dto.QuestionStudentDto;
import com.example.quiz.dto.QuizDto;
import com.example.quiz.model.Quiz;
import com.example.quiz.service.QuestionService;
import com.example.quiz.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "*")
public class QuizController {

    private final QuizService quizService;
    private final QuestionService questionService;

    public QuizController(QuizService quizService, QuestionService questionService) {
        this.quizService = quizService;
        this.questionService = questionService;
    }

    @GetMapping
    public ResponseEntity<List<QuizDto>> getAllPublished(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(quizService.getPublishedQuizzes(category, difficulty, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.getQuizDtoById(id));
    }

    @GetMapping("/{id}/questions")
    public ResponseEntity<List<QuestionStudentDto>> getQuestionsForStudent(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestionsForStudent(id));
    }

    // Legacy fallback mapping for existing tests/admin routes
    @PostMapping
    public ResponseEntity<QuizDto> createLegacy(@RequestBody Quiz quiz) {
        return ResponseEntity.ok(quizService.createQuiz(quiz));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizDto> updateLegacy(@PathVariable Long id, @RequestBody Quiz quiz) {
        return ResponseEntity.ok(quizService.updateQuiz(id, quiz));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLegacy(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }
}
