package com.example.quiz.dto;

import java.time.LocalDateTime;
import java.util.List;

public class StartAttemptDto {
    private Long attemptId;
    private Long quizId;
    private String quizTitle;
    private String category;
    private String difficulty;
    private Integer durationMinutes;
    private Long remainingSeconds;
    private LocalDateTime startedAt;
    private List<QuestionStudentDto> questions;

    public StartAttemptDto() {}

    public StartAttemptDto(Long attemptId, Long quizId, String quizTitle, String category, String difficulty, Integer durationMinutes, Long remainingSeconds, LocalDateTime startedAt, List<QuestionStudentDto> questions) {
        this.attemptId = attemptId;
        this.quizId = quizId;
        this.quizTitle = quizTitle;
        this.category = category;
        this.difficulty = difficulty;
        this.durationMinutes = durationMinutes;
        this.remainingSeconds = remainingSeconds;
        this.startedAt = startedAt;
        this.questions = questions;
    }

    public Long getAttemptId() { return attemptId; }
    public void setAttemptId(Long attemptId) { this.attemptId = attemptId; }

    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }

    public String getQuizTitle() { return quizTitle; }
    public void setQuizTitle(String quizTitle) { this.quizTitle = quizTitle; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public Long getRemainingSeconds() { return remainingSeconds; }
    public void setRemainingSeconds(Long remainingSeconds) { this.remainingSeconds = remainingSeconds; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public List<QuestionStudentDto> getQuestions() { return questions; }
    public void setQuestions(List<QuestionStudentDto> questions) { this.questions = questions; }
}
