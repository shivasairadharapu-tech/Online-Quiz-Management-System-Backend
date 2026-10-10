package com.example.quiz.service;

import com.example.quiz.dto.*;
import com.example.quiz.exception.BadRequestException;
import com.example.quiz.exception.ResourceNotFoundException;
import com.example.quiz.exception.UnauthorizedException;
import com.example.quiz.model.*;
import com.example.quiz.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AttemptService {

    private final QuizAttemptRepository attemptRepository;
    private final AttemptAnswerRepository attemptAnswerRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final ResultRepository resultRepository;

    public AttemptService(QuizAttemptRepository attemptRepository,
                          AttemptAnswerRepository attemptAnswerRepository,
                          QuizRepository quizRepository,
                          QuestionRepository questionRepository,
                          ResultRepository resultRepository) {
        this.attemptRepository = attemptRepository;
        this.attemptAnswerRepository = attemptAnswerRepository;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.resultRepository = resultRepository;
    }

    @Transactional
    public StartAttemptDto startAttempt(Long quizId, User user) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + quizId));

        if (!Boolean.TRUE.equals(quiz.getPublished())) {
            throw new BadRequestException("This quiz is currently unpublished and not available for attempts.");
        }

        List<Question> questions = questionRepository.findByQuizIdOrderByIdAsc(quizId);
        if (questions.isEmpty()) {
            throw new BadRequestException("This quiz currently has no questions.");
        }

        // Create new attempt
        QuizAttempt attempt = new QuizAttempt(user, quiz);
        attempt = attemptRepository.save(attempt);

        List<QuestionStudentDto> studentQuestions = questions.stream()
                .map(q -> new QuestionStudentDto(
                        q.getId(),
                        q.getQuestion(),
                        q.getOptionA(),
                        q.getOptionB(),
                        q.getOptionC(),
                        q.getOptionD(),
                        q.getMarks()
                ))
                .toList();

        long durationSec = (long) quiz.getDurationMinutes() * 60;

        return new StartAttemptDto(
                attempt.getId(),
                quiz.getId(),
                quiz.getTitle(),
                quiz.getCategory(),
                quiz.getDifficulty(),
                quiz.getDurationMinutes(),
                durationSec,
                attempt.getStartedAt(),
                studentQuestions
        );
    }

    public StartAttemptDto getAttemptStatus(Long attemptId, User user) {
        QuizAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Attempt not found with id: " + attemptId));

        if (!attempt.getUser().getId().equals(user.getId()) && !"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new UnauthorizedException("You are not authorized to view this attempt.");
        }

        Quiz quiz = attempt.getQuiz();
        List<Question> questions = questionRepository.findByQuizIdOrderByIdAsc(quiz.getId());
        List<QuestionStudentDto> studentQuestions = questions.stream()
                .map(q -> new QuestionStudentDto(
                        q.getId(),
                        q.getQuestion(),
                        q.getOptionA(),
                        q.getOptionB(),
                        q.getOptionC(),
                        q.getOptionD(),
                        q.getMarks()
                ))
                .toList();

        long maxSeconds = (long) quiz.getDurationMinutes() * 60;
        long elapsedSeconds = Duration.between(attempt.getStartedAt(), LocalDateTime.now()).getSeconds();
        long remaining = Math.max(0, maxSeconds - elapsedSeconds);

        return new StartAttemptDto(
                attempt.getId(),
                quiz.getId(),
                quiz.getTitle(),
                quiz.getCategory(),
                quiz.getDifficulty(),
                quiz.getDurationMinutes(),
                remaining,
                attempt.getStartedAt(),
                studentQuestions
        );
    }

    @Transactional
    public AttemptResultDto submitAttempt(Long attemptId, SubmitAttemptRequest submitRequest, User user) {
        QuizAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Attempt not found with id: " + attemptId));

        // Security check: ensure student owns this attempt
        if (!attempt.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to submit this attempt.");
        }

        // Prevent duplicate submission
        if ("COMPLETED".equalsIgnoreCase(attempt.getStatus())) {
            throw new BadRequestException("This attempt has already been submitted and completed.");
        }

        Quiz quiz = attempt.getQuiz();
        List<Question> questions = questionRepository.findByQuizIdOrderByIdAsc(quiz.getId());
        Map<Long, String> userAnswers = submitRequest != null && submitRequest.getAnswers() != null
                ? submitRequest.getAnswers() : Collections.emptyMap();

        int score = 0;
        int totalMarks = 0;
        int correctCount = 0;
        int incorrectCount = 0;
        int unattemptedCount = 0;

        List<AnswerReviewDto> reviewList = new ArrayList<>();
        List<AttemptAnswer> answersToSave = new ArrayList<>();

        for (Question q : questions) {
            int qMarks = q.getMarks() != null && q.getMarks() > 0 ? q.getMarks() : 1;
            totalMarks += qMarks;

            String selected = userAnswers.get(q.getId());
            if (selected != null) {
                selected = selected.trim().toUpperCase();
            }

            boolean isCorrect = selected != null && selected.equalsIgnoreCase(q.getCorrectAnswer().trim());
            int obtained = isCorrect ? qMarks : 0;

            if (selected == null || selected.isEmpty()) {
                unattemptedCount++;
            } else if (isCorrect) {
                correctCount++;
                score += obtained;
            } else {
                incorrectCount++;
            }

            AttemptAnswer attemptAnswer = new AttemptAnswer(attempt, q, selected, isCorrect, obtained);
            answersToSave.add(attemptAnswer);

            reviewList.add(new AnswerReviewDto(
                    q.getId(),
                    q.getQuestion(),
                    q.getOptionA(),
                    q.getOptionB(),
                    q.getOptionC(),
                    q.getOptionD(),
                    selected,
                    q.getCorrectAnswer(),
                    isCorrect,
                    obtained,
                    qMarks
            ));
        }

        attemptAnswerRepository.saveAll(answersToSave);

        double percentage = totalMarks > 0 ? ((double) score / totalMarks) * 100.0 : 0.0;
        attempt.setSubmittedAt(LocalDateTime.now());
        attempt.setScore(score);
        attempt.setTotalMarks(totalMarks);
        attempt.setPercentage(Math.round(percentage * 10.0) / 10.0);
        attempt.setStatus("COMPLETED");
        attempt = attemptRepository.save(attempt);

        // Also record to legacy Result table for compatibility
        resultRepository.save(new Result(user, quiz, score, totalMarks));

        // Generate encouraging feedback
        String feedback = generateFeedback(percentage);

        AttemptResultDto resultDto = new AttemptResultDto();
        resultDto.setAttemptId(attempt.getId());
        resultDto.setQuizId(quiz.getId());
        resultDto.setQuizTitle(quiz.getTitle());
        resultDto.setCategory(quiz.getCategory());
        resultDto.setDifficulty(quiz.getDifficulty());
        resultDto.setScore(score);
        resultDto.setTotalMarks(totalMarks);
        resultDto.setPercentage(attempt.getPercentage());
        resultDto.setCorrectCount(correctCount);
        resultDto.setIncorrectCount(incorrectCount);
        resultDto.setUnattemptedCount(unattemptedCount);
        resultDto.setStatus("COMPLETED");
        resultDto.setFeedback(feedback);
        resultDto.setStartedAt(attempt.getStartedAt());
        resultDto.setSubmittedAt(attempt.getSubmittedAt());
        resultDto.setAnswersReview(reviewList);

        return resultDto;
    }

    private String generateFeedback(double percentage) {
        if (percentage >= 90.0) {
            return "Outstanding performance! You have demonstrated exceptional mastery of this topic.";
        } else if (percentage >= 75.0) {
            return "Great work! You have a solid understanding of these concepts. Keep it up!";
        } else if (percentage >= 50.0) {
            return "Good effort! You have passed the quiz, but there is still room for improvement.";
        } else {
            return "Keep practicing! Review the concepts and correct answers, then try again to boost your score.";
        }
    }
}
