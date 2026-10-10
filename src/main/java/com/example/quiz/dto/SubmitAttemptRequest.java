package com.example.quiz.dto;

import java.util.Map;

public class SubmitAttemptRequest {
    // Map of questionId -> selected answer ("A", "B", "C", or "D")
    private Map<Long, String> answers;

    public SubmitAttemptRequest() {}

    public SubmitAttemptRequest(Map<Long, String> answers) {
        this.answers = answers;
    }

    public Map<Long, String> getAnswers() { return answers; }
    public void setAnswers(Map<Long, String> answers) { this.answers = answers; }
}
