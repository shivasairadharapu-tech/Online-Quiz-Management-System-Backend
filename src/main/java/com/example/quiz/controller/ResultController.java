package com.example.quiz.controller;

import com.example.quiz.dto.AttemptResultDto;
import com.example.quiz.dto.StudentAnalyticsDto;
import com.example.quiz.dto.StudentResultDto;
import com.example.quiz.dto.SubmitAttemptRequest;
import com.example.quiz.model.Quiz;
import com.example.quiz.model.Result;
import com.example.quiz.model.User;
import com.example.quiz.repository.QuizRepository;
import com.example.quiz.repository.ResultRepository;
import com.example.quiz.repository.UserRepository;
import com.example.quiz.service.AttemptService;
import com.example.quiz.service.AuthService;
import com.example.quiz.service.ResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "*")
public class ResultController {

    private final ResultService resultService;
    private final AuthService authService;
    private final AttemptService attemptService;
    private final ResultRepository resultRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;

    public ResultController(ResultService resultService,
                            AuthService authService,
                            AttemptService attemptService,
                            ResultRepository resultRepository,
                            UserRepository userRepository,
                            QuizRepository quizRepository) {
        this.resultService = resultService;
        this.authService = authService;
        this.attemptService = attemptService;
        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
    }

    @GetMapping("/{attemptId}")
    public ResponseEntity<AttemptResultDto> getResult(@PathVariable Long attemptId) {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(resultService.getAttemptResult(attemptId, user));
    }

    @GetMapping("/user/me")
    public ResponseEntity<StudentAnalyticsDto> getMyAnalytics() {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(resultService.getStudentPerformance(user));
    }

    @GetMapping("/user/me/history")
    public ResponseEntity<List<StudentResultDto>> getMyHistory() {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(resultService.getStudentHistory(user));
    }

    // Legacy endpoint: GET /api/results/user/{id}
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getLegacyUserResults(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            List<StudentResultDto> history = resultService.getStudentHistory(user);
            if (!history.isEmpty()) {
                return ResponseEntity.ok(history);
            }
        }

        // Fallback to legacy results table
        List<Map<String, Object>> response = new ArrayList<>();
        resultRepository.findByUserIdOrderByAttemptedAtDesc(id).forEach(r -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", r.getId());
            item.put("quizTitle", r.getQuiz().getTitle());
            item.put("score", r.getScore());
            item.put("total", r.getTotal());
            item.put("attemptedAt", r.getAttemptedAt());
            response.add(item);
        });
        return ResponseEntity.ok(response);
    }

    // Legacy endpoint: GET /api/results
    @GetMapping
    public ResponseEntity<?> getLegacyAllResults() {
        return ResponseEntity.ok(resultService.getAllResultsForAdmin(null, null));
    }

    // Legacy endpoint: POST /api/results
    @PostMapping
    public ResponseEntity<?> submitLegacy(@RequestBody Map<String, Object> data) {
        try {
            Long uid = Long.valueOf(data.get("userId").toString());
            Long qid = Long.valueOf(data.get("quizId").toString());
            User user = userRepository.findById(uid).orElseThrow();
            Quiz quiz = quizRepository.findById(qid).orElseThrow();

            @SuppressWarnings("unchecked")
            Map<String, String> answersMap = (Map<String, String>) data.get("answers");
            Map<Long, String> parsedAnswers = new HashMap<>();
            if (answersMap != null) {
                answersMap.forEach((k, v) -> parsedAnswers.put(Long.valueOf(k), v));
            }

            var startDto = attemptService.startAttempt(qid, user);
            var resultDto = attemptService.submitAttempt(startDto.getAttemptId(), new SubmitAttemptRequest(parsedAnswers), user);

            Map<String, Object> res = new HashMap<>();
            res.put("id", resultDto.getAttemptId());
            res.put("score", resultDto.getScore());
            res.put("total", resultDto.getTotalMarks());
            res.put("percentage", resultDto.getPercentage());
            return ResponseEntity.ok(res);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("message", "Unable to submit quiz: " + ex.getMessage()));
        }
    }
}
