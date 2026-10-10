package com.example.quiz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "attempt_answers")
public class AttemptAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id")
    @JsonIgnore
    private QuizAttempt attempt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    private String selectedAnswer; // "A", "B", "C", "D" or null

    private Boolean isCorrect = false;

    private Integer marksObtained = 0;

    public AttemptAnswer() {}

    public AttemptAnswer(QuizAttempt attempt, Question question, String selectedAnswer, Boolean isCorrect, Integer marksObtained) {
        this.attempt = attempt;
        this.question = question;
        this.selectedAnswer = selectedAnswer;
        this.isCorrect = isCorrect != null ? isCorrect : false;
        this.marksObtained = marksObtained != null ? marksObtained : 0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public QuizAttempt getAttempt() { return attempt; }
    public void setAttempt(QuizAttempt attempt) { this.attempt = attempt; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }

    public String getSelectedAnswer() { return selectedAnswer; }
    public void setSelectedAnswer(String selectedAnswer) { this.selectedAnswer = selectedAnswer; }

    public Boolean getIsCorrect() { return isCorrect != null ? isCorrect : false; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }

    public Integer getMarksObtained() { return marksObtained != null ? marksObtained : 0; }
    public void setMarksObtained(Integer marksObtained) { this.marksObtained = marksObtained; }
}
