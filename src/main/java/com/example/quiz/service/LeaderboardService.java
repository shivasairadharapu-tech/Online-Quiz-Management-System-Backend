package com.example.quiz.service;

import com.example.quiz.dto.LeaderboardEntryDto;
import com.example.quiz.model.QuizAttempt;
import com.example.quiz.model.User;
import com.example.quiz.repository.QuizAttemptRepository;
import com.example.quiz.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {

    private final QuizAttemptRepository attemptRepository;
    private final UserRepository userRepository;

    public LeaderboardService(QuizAttemptRepository attemptRepository, UserRepository userRepository) {
        this.attemptRepository = attemptRepository;
        this.userRepository = userRepository;
    }

    public List<LeaderboardEntryDto> getLeaderboard() {
        List<QuizAttempt> completedAttempts = attemptRepository.findAll().stream()
                .filter(a -> "COMPLETED".equalsIgnoreCase(a.getStatus()))
                .collect(Collectors.toList());

        // Group attempts by User
        Map<User, List<QuizAttempt>> userAttemptsMap = completedAttempts.stream()
                .collect(Collectors.groupingBy(QuizAttempt::getUser));

        List<LeaderboardEntryDto> entries = new ArrayList<>();

        for (Map.Entry<User, List<QuizAttempt>> entry : userAttemptsMap.entrySet()) {
            User user = entry.getKey();
            List<QuizAttempt> attempts = entry.getValue();

            int totalScore = attempts.stream().mapToInt(QuizAttempt::getScore).sum();
            int totalMarks = attempts.stream().mapToInt(QuizAttempt::getTotalMarks).sum();
            int completedCount = attempts.size();
            double avgPercentage = totalMarks > 0 ? ((double) totalScore / totalMarks) * 100.0 : 0.0;

            entries.add(new LeaderboardEntryDto(
                    0,
                    user.getId(),
                    user.getName() != null ? user.getName() : "Student #" + user.getId(),
                    totalScore,
                    totalMarks,
                    completedCount,
                    Math.round(avgPercentage * 10.0) / 10.0
            ));
        }

        // Sort descending by totalScore, then avgPercentage
        entries.sort((a, b) -> {
            if (b.getTotalScore() != a.getTotalScore()) {
                return Integer.compare(b.getTotalScore(), a.getTotalScore());
            }
            return Double.compare(b.getAveragePercentage(), a.getAveragePercentage());
        });

        // Assign ranks
        for (int i = 0; i < entries.size(); i++) {
            entries.get(i).setRank(i + 1);
        }

        return entries;
    }
}
