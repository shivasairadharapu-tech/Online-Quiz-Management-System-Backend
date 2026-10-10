package com.example.quiz.service;

import com.example.quiz.dto.AnswerReviewDto;
import com.example.quiz.dto.AttemptResultDto;
import com.example.quiz.dto.StudentAnalyticsDto;
import com.example.quiz.dto.StudentResultDto;
import com.example.quiz.exception.ResourceNotFoundException;
import com.example.quiz.exception.UnauthorizedException;
import com.example.quiz.model.*;
import com.example.quiz.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResultService {

    private final QuizAttemptRepository attemptRepository;
    private final AttemptAnswerRepository attemptAnswerRepository;
    private final QuizRepository quizRepository;

    public ResultService(QuizAttemptRepository attemptRepository,
                         AttemptAnswerRepository attemptAnswerRepository,
                         QuizRepository quizRepository) {
        this.attemptRepository = attemptRepository;
        this.attemptAnswerRepository = attemptAnswerRepository;
        this.quizRepository = quizRepository;
    }

    public AttemptResultDto getAttemptResult(Long attemptId, User user) {
        QuizAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Attempt not found with id: " + attemptId));

        if (!attempt.getUser().getId().equals(user.getId()) && !"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new UnauthorizedException("You are not authorized to view this result.");
        }

        List<AttemptAnswer> answers = attemptAnswerRepository.findByAttemptIdOrderByIdAsc(attemptId);
        List<AnswerReviewDto> reviewList = answers.stream().map(a -> {
            Question q = a.getQuestion();
            int qMarks = q.getMarks() != null && q.getMarks() > 0 ? q.getMarks() : 1;
            return new AnswerReviewDto(
                    q.getId(),
                    q.getQuestion(),
                    q.getOptionA(),
                    q.getOptionB(),
                    q.getOptionC(),
                    q.getOptionD(),
                    a.getSelectedAnswer(),
                    q.getCorrectAnswer(),
                    a.getIsCorrect(),
                    a.getMarksObtained(),
                    qMarks
            );
        }).collect(Collectors.toList());

        int correctCount = (int) answers.stream().filter(a -> Boolean.TRUE.equals(a.getIsCorrect())).count();
        int unattemptedCount = (int) answers.stream().filter(a -> a.getSelectedAnswer() == null || a.getSelectedAnswer().isEmpty()).count();
        int incorrectCount = answers.size() - correctCount - unattemptedCount;

        String feedback = generateFeedback(attempt.getPercentage() != null ? attempt.getPercentage() : 0.0);

        AttemptResultDto dto = new AttemptResultDto();
        dto.setAttemptId(attempt.getId());
        dto.setQuizId(attempt.getQuiz().getId());
        dto.setQuizTitle(attempt.getQuiz().getTitle());
        dto.setCategory(attempt.getQuiz().getCategory());
        dto.setDifficulty(attempt.getQuiz().getDifficulty());
        dto.setScore(attempt.getScore());
        dto.setTotalMarks(attempt.getTotalMarks());
        dto.setPercentage(attempt.getPercentage());
        dto.setCorrectCount(correctCount);
        dto.setIncorrectCount(incorrectCount);
        dto.setUnattemptedCount(unattemptedCount);
        dto.setStatus(attempt.getStatus());
        dto.setFeedback(feedback);
        dto.setStartedAt(attempt.getStartedAt());
        dto.setSubmittedAt(attempt.getSubmittedAt());
        dto.setAnswersReview(reviewList);

        return dto;
    }

    public List<StudentResultDto> getStudentHistory(User user) {
        List<QuizAttempt> attempts = attemptRepository.findByUserIdOrderByStartedAtDesc(user.getId());
        return attempts.stream()
                .filter(a -> "COMPLETED".equalsIgnoreCase(a.getStatus()))
                .map(this::toStudentResultDto)
                .collect(Collectors.toList());
    }

    public StudentAnalyticsDto getStudentPerformance(User user) {
        List<QuizAttempt> completed = attemptRepository.findByUserIdAndStatusOrderByStartedAtDesc(user.getId(), "COMPLETED");

        StudentAnalyticsDto dto = new StudentAnalyticsDto();
        dto.setTotalQuizzesAttempted(completed.size());

        long availableCount = quizRepository.countByPublishedTrue();
        dto.setAvailableQuizzes(availableCount);

        if (completed.isEmpty()) {
            dto.setAverageScore(0.0);
            dto.setHighestScore(0.0);
            dto.setCategoryPerformance(Collections.emptyMap());
            dto.setScoreTrends(Collections.emptyList());
            dto.setRecentAttempts(Collections.emptyList());
            return dto;
        }

        double avgPercentage = completed.stream().mapToDouble(a -> a.getPercentage() != null ? a.getPercentage() : 0.0).average().orElse(0.0);
        double maxPercentage = completed.stream().mapToDouble(a -> a.getPercentage() != null ? a.getPercentage() : 0.0).max().orElse(0.0);

        dto.setAverageScore(Math.round(avgPercentage * 10.0) / 10.0);
        dto.setHighestScore(Math.round(maxPercentage * 10.0) / 10.0);

        // Category breakdown
        Map<String, List<Double>> catMap = new HashMap<>();
        for (QuizAttempt a : completed) {
            String cat = a.getQuiz().getCategory();
            if (cat == null) cat = "General";
            catMap.computeIfAbsent(cat, k -> new ArrayList<>()).add(a.getPercentage() != null ? a.getPercentage() : 0.0);
        }

        Map<String, Double> categoryAverages = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : catMap.entrySet()) {
            double catAvg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            categoryAverages.put(entry.getKey(), Math.round(catAvg * 10.0) / 10.0);
        }
        dto.setCategoryPerformance(categoryAverages);

        // Trends (reverse chrono -> oldest to newest for charts)
        List<QuizAttempt> chrono = new ArrayList<>(completed);
        Collections.reverse(chrono);
        List<Map<String, Object>> trends = chrono.stream().map(a -> {
            Map<String, Object> point = new HashMap<>();
            point.put("attemptId", a.getId());
            point.put("quizTitle", a.getQuiz().getTitle());
            point.put("percentage", a.getPercentage());
            point.put("score", a.getScore());
            point.put("totalMarks", a.getTotalMarks());
            point.put("date", a.getSubmittedAt() != null ? a.getSubmittedAt().toString() : a.getStartedAt().toString());
            return point;
        }).collect(Collectors.toList());
        dto.setScoreTrends(trends);

        // Recent attempts (top 5)
        List<AttemptResultDto> recent = completed.stream().limit(5).map(a -> {
            AttemptResultDto res = new AttemptResultDto();
            res.setAttemptId(a.getId());
            res.setQuizId(a.getQuiz().getId());
            res.setQuizTitle(a.getQuiz().getTitle());
            res.setCategory(a.getQuiz().getCategory());
            res.setDifficulty(a.getQuiz().getDifficulty());
            res.setScore(a.getScore());
            res.setTotalMarks(a.getTotalMarks());
            res.setPercentage(a.getPercentage());
            res.setStatus(a.getStatus());
            res.setSubmittedAt(a.getSubmittedAt());
            return res;
        }).collect(Collectors.toList());
        dto.setRecentAttempts(recent);

        return dto;
    }

    public List<StudentResultDto> getAllResultsForAdmin(Long quizId, String search) {
        List<QuizAttempt> attempts;
        if (quizId != null && quizId > 0) {
            attempts = attemptRepository.findByQuizIdOrderByStartedAtDesc(quizId);
        } else {
            attempts = attemptRepository.findAll().stream()
                    .sorted((a, b) -> b.getStartedAt().compareTo(a.getStartedAt()))
                    .collect(Collectors.toList());
        }

        return attempts.stream()
                .filter(a -> "COMPLETED".equalsIgnoreCase(a.getStatus()))
                .filter(a -> {
                    if (search == null || search.trim().isEmpty()) return true;
                    String s = search.toLowerCase();
                    return (a.getUser().getName() != null && a.getUser().getName().toLowerCase().contains(s)) ||
                           (a.getUser().getEmail() != null && a.getUser().getEmail().toLowerCase().contains(s)) ||
                           (a.getQuiz().getTitle() != null && a.getQuiz().getTitle().toLowerCase().contains(s));
                })
                .map(this::toStudentResultDto)
                .collect(Collectors.toList());
    }

    private StudentResultDto toStudentResultDto(QuizAttempt a) {
        StudentResultDto dto = new StudentResultDto();
        dto.setId(a.getId());
        dto.setAttemptId(a.getId());
        dto.setStudentId(a.getUser().getId());
        dto.setStudentName(a.getUser().getName());
        dto.setStudentEmail(a.getUser().getEmail());
        dto.setQuizId(a.getQuiz().getId());
        dto.setQuizTitle(a.getQuiz().getTitle());
        dto.setCategory(a.getQuiz().getCategory());
        dto.setScore(a.getScore());
        dto.setTotalMarks(a.getTotalMarks());
        dto.setPercentage(a.getPercentage());
        dto.setStatus(a.getStatus());
        dto.setAttemptedAt(a.getSubmittedAt() != null ? a.getSubmittedAt() : a.getStartedAt());
        return dto;
    }

    private String generateFeedback(double percentage) {
        if (percentage >= 90.0) return "Outstanding performance! You have mastered this quiz.";
        if (percentage >= 75.0) return "Well done! Solid grasp of concepts.";
        if (percentage >= 50.0) return "Good effort! Practice more to improve.";
        return "Keep practicing! Review the concepts and try again.";
    }
}
