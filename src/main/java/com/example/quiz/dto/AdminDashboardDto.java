package com.example.quiz.dto;

import java.util.List;
import java.util.Map;

public class AdminDashboardDto {
    private long totalStudents;
    private long totalQuizzes;
    private long publishedQuizzes;
    private long totalCompletedAttempts;
    private double platformAveragePercentage;
    private List<StudentResultDto> recentAttempts;
    private Map<String, Long> categoryDistribution;
    private Map<String, Double> categoryAverageScores;

    public AdminDashboardDto() {}

    public long getTotalStudents() { return totalStudents; }
    public void setTotalStudents(long totalStudents) { this.totalStudents = totalStudents; }

    public long getTotalQuizzes() { return totalQuizzes; }
    public void setTotalQuizzes(long totalQuizzes) { this.totalQuizzes = totalQuizzes; }

    public long getPublishedQuizzes() { return publishedQuizzes; }
    public void setPublishedQuizzes(long publishedQuizzes) { this.publishedQuizzes = publishedQuizzes; }

    public long getTotalCompletedAttempts() { return totalCompletedAttempts; }
    public void setTotalCompletedAttempts(long totalCompletedAttempts) { this.totalCompletedAttempts = totalCompletedAttempts; }

    public double getPlatformAveragePercentage() { return platformAveragePercentage; }
    public void setPlatformAveragePercentage(double platformAveragePercentage) { this.platformAveragePercentage = platformAveragePercentage; }

    public List<StudentResultDto> getRecentAttempts() { return recentAttempts; }
    public void setRecentAttempts(List<StudentResultDto> recentAttempts) { this.recentAttempts = recentAttempts; }

    public Map<String, Long> getCategoryDistribution() { return categoryDistribution; }
    public void setCategoryDistribution(Map<String, Long> categoryDistribution) { this.categoryDistribution = categoryDistribution; }

    public Map<String, Double> getCategoryAverageScores() { return categoryAverageScores; }
    public void setCategoryAverageScores(Map<String, Double> categoryAverageScores) { this.categoryAverageScores = categoryAverageScores; }
}
