package com.example.quiz.dto;

import java.util.List;
import java.util.Map;

public class StudentAnalyticsDto {
    private long totalQuizzesAttempted;
    private double averageScore;
    private double highestScore;
    private long availableQuizzes;
    private Map<String, Double> categoryPerformance;
    private List<Map<String, Object>> scoreTrends;
    private List<AttemptResultDto> recentAttempts;

    public StudentAnalyticsDto() {}

    public long getTotalQuizzesAttempted() { return totalQuizzesAttempted; }
    public void setTotalQuizzesAttempted(long totalQuizzesAttempted) { this.totalQuizzesAttempted = totalQuizzesAttempted; }

    public double getAverageScore() { return averageScore; }
    public void setAverageScore(double averageScore) { this.averageScore = averageScore; }

    public double getHighestScore() { return highestScore; }
    public void setHighestScore(double highestScore) { this.highestScore = highestScore; }

    public long getAvailableQuizzes() { return availableQuizzes; }
    public void setAvailableQuizzes(long availableQuizzes) { this.availableQuizzes = availableQuizzes; }

    public Map<String, Double> getCategoryPerformance() { return categoryPerformance; }
    public void setCategoryPerformance(Map<String, Double> categoryPerformance) { this.categoryPerformance = categoryPerformance; }

    public List<Map<String, Object>> getScoreTrends() { return scoreTrends; }
    public void setScoreTrends(List<Map<String, Object>> scoreTrends) { this.scoreTrends = scoreTrends; }

    public List<AttemptResultDto> getRecentAttempts() { return recentAttempts; }
    public void setRecentAttempts(List<AttemptResultDto> recentAttempts) { this.recentAttempts = recentAttempts; }
}
