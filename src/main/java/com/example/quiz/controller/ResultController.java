package com.example.quiz.controller;

import com.example.quiz.model.*;
import com.example.quiz.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "*")
public class ResultController {
    private final ResultRepository results;
    private final UserRepository users;
    private final QuizRepository quizzes;
    private final QuestionRepository questions;

    public ResultController(ResultRepository r, UserRepository u, QuizRepository q, QuestionRepository x) {
        results = r; users = u; quizzes = q; questions = x;
    }

    @PostMapping
    public ResponseEntity<?> submit(@RequestBody Map<String, Object> data) {
        try {
            Long uid = Long.valueOf(data.get("userId").toString());
            Long qid = Long.valueOf(data.get("quizId").toString());
            User u = users.findById(uid).orElseThrow();
            Quiz q = quizzes.findById(qid).orElseThrow();

            @SuppressWarnings("unchecked")
            Map<String, String> answers = (Map<String, String>) data.get("answers");
            List<Question> qs = questions.findByQuizId(qid);
            int score = 0;
            for (Question x : qs) {
                String answer = answers == null ? null : answers.get(String.valueOf(x.getId()));
                if (answer != null && x.getCorrectAnswer().equalsIgnoreCase(answer)) score++;
            }

            Result saved = results.save(new Result(u, q, score, qs.size()));
            Map<String, Object> response = new HashMap<>();
            response.put("id", saved.getId());
            response.put("score", score);
            response.put("total", qs.size());
            response.put("percentage", qs.isEmpty() ? 0.0 : (score * 100.0 / qs.size()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Unable to submit quiz"));
        }
    }

    @GetMapping("/user/{id}")
    public List<Map<String, Object>> user(@PathVariable Long id) {
        List<Map<String, Object>> response = new ArrayList<>();
        results.findByUserIdOrderByAttemptedAtDesc(id).forEach(r -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", r.getId());
            item.put("quizTitle", r.getQuiz().getTitle());
            item.put("score", r.getScore());
            item.put("total", r.getTotal());
            item.put("attemptedAt", r.getAttemptedAt());
            response.add(item);
        });
        return response;
    }

    @GetMapping
    public List<Map<String, Object>> all() {
        List<Map<String, Object>> response = new ArrayList<>();
        results.findAll().forEach(r -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", r.getId());
            item.put("student", r.getUser().getName());
            item.put("email", r.getUser().getEmail());
            item.put("quizTitle", r.getQuiz().getTitle());
            item.put("score", r.getScore());
            item.put("total", r.getTotal());
            item.put("attemptedAt", r.getAttemptedAt());
            response.add(item);
        });
        return response;
    }
}
