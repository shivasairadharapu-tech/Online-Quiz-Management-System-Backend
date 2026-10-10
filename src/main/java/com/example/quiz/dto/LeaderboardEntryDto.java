package com.example.quiz.dto;

public class LeaderboardEntryDto {
    private int rank;
    private Long studentId;
    private String studentName;
    private int totalScore;
    private int totalPossibleMarks;
    private int completedQuizzes;
    private double averagePercentage;

    public LeaderboardEntryDto() {}

    public LeaderboardEntryDto(int rank, Long studentId, String studentName, int totalScore, int totalPossibleMarks, int completedQuizzes, double averagePercentage) {
        this.rank = rank;
        this.studentId = studentId;
        this.studentName = studentName;
        this.totalScore = totalScore;
        this.totalPossibleMarks = totalPossibleMarks;
        this.completedQuizzes = completedQuizzes;
        this.averagePercentage = averagePercentage;
    }

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }

    public int getTotalPossibleMarks() { return totalPossibleMarks; }
    public void setTotalPossibleMarks(int totalPossibleMarks) { this.totalPossibleMarks = totalPossibleMarks; }

    public int getCompletedQuizzes() { return completedQuizzes; }
    public void setCompletedQuizzes(int completedQuizzes) { this.completedQuizzes = completedQuizzes; }

    public double getAveragePercentage() { return averagePercentage; }
    public void setAveragePercentage(double averagePercentage) { this.averagePercentage = averagePercentage; }
}
