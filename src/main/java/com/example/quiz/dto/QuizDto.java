package com.example.quiz.dto;

import java.time.LocalDateTime;

public class QuizDto {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String difficulty;
    private Integer durationMinutes;
    private Boolean published;
    private LocalDateTime createdAt;
    private long questionCount;

    public QuizDto() {}

    public QuizDto(Long id, String title, String description, String category, String difficulty, Integer durationMinutes, Boolean published, LocalDateTime createdAt, long questionCount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.difficulty = difficulty;
        this.durationMinutes = durationMinutes;
        this.published = published;
        this.createdAt = createdAt;
        this.questionCount = questionCount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public Boolean getPublished() { return published; }
    public void setPublished(Boolean published) { this.published = published; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public long getQuestionCount() { return questionCount; }
    public void setQuestionCount(long questionCount) { this.questionCount = questionCount; }
}
