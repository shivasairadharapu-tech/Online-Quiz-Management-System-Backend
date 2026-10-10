package com.example.quiz.repository;

import com.example.quiz.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByPublishedTrue();
    List<Quiz> findByCategoryAndPublishedTrue(String category);
    long countByPublishedTrue();
    List<Quiz> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String title, String category);
}
