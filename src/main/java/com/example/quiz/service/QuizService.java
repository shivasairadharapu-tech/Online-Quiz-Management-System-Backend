package com.example.quiz.service;

import com.example.quiz.dto.QuizDto;
import com.example.quiz.exception.ResourceNotFoundException;
import com.example.quiz.model.Quiz;
import com.example.quiz.repository.QuestionRepository;
import com.example.quiz.repository.QuizAttemptRepository;
import com.example.quiz.repository.QuizRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final QuizAttemptRepository attemptRepository;

    public QuizService(QuizRepository quizRepository,
                       QuestionRepository questionRepository,
                       QuizAttemptRepository attemptRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.attemptRepository = attemptRepository;
    }

    public List<QuizDto> getPublishedQuizzes(String category, String difficulty, String search) {
        return quizRepository.findAll().stream()
                .filter(q -> Boolean.TRUE.equals(q.getPublished()))
                .filter(q -> category == null || category.equalsIgnoreCase("All") || category.equalsIgnoreCase(q.getCategory()))
                .filter(q -> difficulty == null || difficulty.equalsIgnoreCase("All") || difficulty.equalsIgnoreCase(q.getDifficulty()))
                .filter(q -> {
                    if (search == null || search.trim().isEmpty()) return true;
                    String s = search.toLowerCase();
                    return (q.getTitle() != null && q.getTitle().toLowerCase().contains(s)) ||
                           (q.getDescription() != null && q.getDescription().toLowerCase().contains(s)) ||
                           (q.getCategory() != null && q.getCategory().toLowerCase().contains(s));
                })
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<QuizDto> getAllQuizzesForAdmin() {
        return quizRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public QuizDto getQuizDtoById(Long id) {
        Quiz q = getQuizEntityById(id);
        return toDto(q);
    }

    public Quiz getQuizEntityById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + id));
    }

    @Transactional
    public QuizDto createQuiz(Quiz quiz) {
        quiz.setId(null);
        if (quiz.getPublished() == null) quiz.setPublished(true);
        if (quiz.getDurationMinutes() == null || quiz.getDurationMinutes() <= 0) quiz.setDurationMinutes(10);
        if (quiz.getDifficulty() == null) quiz.setDifficulty("Easy");
        Quiz saved = quizRepository.save(quiz);
        return toDto(saved);
    }

    @Transactional
    public QuizDto updateQuiz(Long id, Quiz updated) {
        Quiz existing = getQuizEntityById(id);
        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setCategory(updated.getCategory());
        existing.setDifficulty(updated.getDifficulty());
        if (updated.getDurationMinutes() != null && updated.getDurationMinutes() > 0) {
            existing.setDurationMinutes(updated.getDurationMinutes());
        }
        if (updated.getPublished() != null) {
            existing.setPublished(updated.getPublished());
        }
        Quiz saved = quizRepository.save(existing);
        return toDto(saved);
    }

    @Transactional
    public QuizDto togglePublish(Long id, Boolean published) {
        Quiz existing = getQuizEntityById(id);
        if (published != null) {
            existing.setPublished(published);
        } else {
            existing.setPublished(!Boolean.TRUE.equals(existing.getPublished()));
        }
        Quiz saved = quizRepository.save(existing);
        return toDto(saved);
    }

    @Transactional
    public void deleteQuiz(Long id) {
        Quiz existing = getQuizEntityById(id);
        // Note: Check if quiz has attempts to preserve historical records safely
        long attemptsCount = attemptRepository.countByStatus("COMPLETED");
        // We delete questions belonging to this quiz or unpublish it if attempts exist
        questionRepository.deleteByQuizId(id);
        quizRepository.delete(existing);
    }

    public QuizDto toDto(Quiz q) {
        long count = questionRepository.countByQuizId(q.getId());
        return new QuizDto(
                q.getId(),
                q.getTitle(),
                q.getDescription(),
                q.getCategory(),
                q.getDifficulty(),
                q.getDurationMinutes(),
                q.getPublished(),
                q.getCreatedAt(),
                count
        );
    }
}
