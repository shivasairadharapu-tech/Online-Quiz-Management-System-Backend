package com.example.quiz.service;

import com.example.quiz.dto.AdminDashboardDto;
import com.example.quiz.dto.StudentResultDto;
import com.example.quiz.dto.UserDto;
import com.example.quiz.model.QuizAttempt;
import com.example.quiz.model.User;
import com.example.quiz.repository.QuizAttemptRepository;
import com.example.quiz.repository.QuizRepository;
import com.example.quiz.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final QuizAttemptRepository attemptRepository;

    public AdminService(UserRepository userRepository,
                        QuizRepository quizRepository,
                        QuizAttemptRepository attemptRepository) {
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
        this.attemptRepository = attemptRepository;
    }

    public AdminDashboardDto getDashboardStats() {
        long totalStudents = userRepository.countByRole("STUDENT");
        long totalQuizzes = quizRepository.count();
        long publishedQuizzes = quizRepository.countByPublishedTrue();
        long totalCompleted = attemptRepository.countByStatus("COMPLETED");

        List<QuizAttempt> allCompleted = attemptRepository.findAll().stream()
                .filter(a -> "COMPLETED".equalsIgnoreCase(a.getStatus()))
                .collect(Collectors.toList());

        double platformAvg = allCompleted.stream()
                .mapToDouble(a -> a.getPercentage() != null ? a.getPercentage() : 0.0)
                .average()
                .orElse(0.0);

        // Recent attempts (top 6)
        List<StudentResultDto> recent = allCompleted.stream()
                .sorted((a, b) -> b.getStartedAt().compareTo(a.getStartedAt()))
                .limit(6)
                .map(this::toResultDto)
                .collect(Collectors.toList());

        // Category distribution
        Map<String, Long> catDist = quizRepository.findAll().stream()
                .collect(Collectors.groupingBy(q -> q.getCategory() != null ? q.getCategory() : "General", Collectors.counting()));

        // Category avg scores
        Map<String, Double> catAvgScores = new HashMap<>();
        Map<String, List<Double>> catScoresMap = new HashMap<>();
        for (QuizAttempt a : allCompleted) {
            String cat = a.getQuiz().getCategory();
            if (cat == null) cat = "General";
            catScoresMap.computeIfAbsent(cat, k -> new ArrayList<>()).add(a.getPercentage() != null ? a.getPercentage() : 0.0);
        }
        for (Map.Entry<String, List<Double>> e : catScoresMap.entrySet()) {
            double avg = e.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            catAvgScores.put(e.getKey(), Math.round(avg * 10.0) / 10.0);
        }

        AdminDashboardDto dto = new AdminDashboardDto();
        dto.setTotalStudents(totalStudents);
        dto.setTotalQuizzes(totalQuizzes);
        dto.setPublishedQuizzes(publishedQuizzes);
        dto.setTotalCompletedAttempts(totalCompleted);
        dto.setPlatformAveragePercentage(Math.round(platformAvg * 10.0) / 10.0);
        dto.setRecentAttempts(recent);
        dto.setCategoryDistribution(catDist);
        dto.setCategoryAverageScores(catAvgScores);

        return dto;
    }

    public List<UserDto> getAllStudents(String search) {
        List<User> students = userRepository.findByRole("STUDENT");

        return students.stream()
                .filter(u -> {
                    if (search == null || search.trim().isEmpty()) return true;
                    String s = search.toLowerCase();
                    return (u.getName() != null && u.getName().toLowerCase().contains(s)) ||
                           (u.getEmail() != null && u.getEmail().toLowerCase().contains(s));
                })
                .map(u -> {
                    UserDto dto = new UserDto(u.getId(), u.getName(), u.getEmail(), u.getRole(), u.getCreatedAt());
                    List<QuizAttempt> attempts = attemptRepository.findByUserIdAndStatusOrderByStartedAtDesc(u.getId(), "COMPLETED");
                    dto.setTotalAttempts(attempts.size());
                    double avg = attempts.stream().mapToDouble(a -> a.getPercentage() != null ? a.getPercentage() : 0.0).average().orElse(0.0);
                    dto.setAverageScore(Math.round(avg * 10.0) / 10.0);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private StudentResultDto toResultDto(QuizAttempt a) {
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
}
