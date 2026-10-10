package com.example.quiz.repository;

import com.example.quiz.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByUserIdOrderByStartedAtDesc(Long userId);
    List<QuizAttempt> findByUserIdAndStatusOrderByStartedAtDesc(Long userId, String status);
    List<QuizAttempt> findByQuizIdOrderByStartedAtDesc(Long quizId);
    Optional<QuizAttempt> findByIdAndUserId(Long id, Long userId);
    long countByStatus(String status);
    long countByUserId(Long userId);
    long countByUserIdAndStatus(Long userId, String status);
}
