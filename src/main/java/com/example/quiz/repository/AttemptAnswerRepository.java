package com.example.quiz.repository;

import com.example.quiz.model.AttemptAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttemptAnswerRepository extends JpaRepository<AttemptAnswer, Long> {
    List<AttemptAnswer> findByAttemptId(Long attemptId);
    List<AttemptAnswer> findByAttemptIdOrderByIdAsc(Long attemptId);
}
