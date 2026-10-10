package com.example.quiz.service;

import com.example.quiz.dto.QuestionAdminDto;
import com.example.quiz.dto.QuestionStudentDto;
import com.example.quiz.exception.ResourceNotFoundException;
import com.example.quiz.model.Question;
import com.example.quiz.model.Quiz;
import com.example.quiz.repository.QuestionRepository;
import com.example.quiz.repository.QuizRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    public QuestionService(QuestionRepository questionRepository, QuizRepository quizRepository) {
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository;
    }

    public List<QuestionStudentDto> getQuestionsForStudent(Long quizId) {
        if (!quizRepository.existsById(quizId)) {
            throw new ResourceNotFoundException("Quiz not found with id: " + quizId);
        }
        return questionRepository.findByQuizIdOrderByIdAsc(quizId).stream()
                .map(q -> new QuestionStudentDto(
                        q.getId(),
                        q.getQuestion(),
                        q.getOptionA(),
                        q.getOptionB(),
                        q.getOptionC(),
                        q.getOptionD(),
                        q.getMarks()
                ))
                .collect(Collectors.toList());
    }

    public List<QuestionAdminDto> getQuestionsForAdmin(Long quizId) {
        if (!quizRepository.existsById(quizId)) {
            throw new ResourceNotFoundException("Quiz not found with id: " + quizId);
        }
        return questionRepository.findByQuizIdOrderByIdAsc(quizId).stream()
                .map(q -> new QuestionAdminDto(
                        q.getId(),
                        quizId,
                        q.getQuestion(),
                        q.getOptionA(),
                        q.getOptionB(),
                        q.getOptionC(),
                        q.getOptionD(),
                        q.getCorrectAnswer(),
                        q.getMarks()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public QuestionAdminDto addQuestion(Long quizId, QuestionAdminDto dto) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + quizId));

        Question q = new Question();
        q.setQuiz(quiz);
        q.setQuestion(dto.getQuestion().trim());
        q.setOptionA(dto.getOptionA().trim());
        q.setOptionB(dto.getOptionB().trim());
        q.setOptionC(dto.getOptionC().trim());
        q.setOptionD(dto.getOptionD().trim());
        q.setCorrectAnswer(dto.getCorrectAnswer().trim().toUpperCase());
        q.setMarks(dto.getMarks() != null && dto.getMarks() > 0 ? dto.getMarks() : 1);

        Question saved = questionRepository.save(q);
        dto.setId(saved.getId());
        dto.setQuizId(quizId);
        return dto;
    }

    @Transactional
    public QuestionAdminDto updateQuestion(Long id, QuestionAdminDto dto) {
        Question existing = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));

        existing.setQuestion(dto.getQuestion().trim());
        existing.setOptionA(dto.getOptionA().trim());
        existing.setOptionB(dto.getOptionB().trim());
        existing.setOptionC(dto.getOptionC().trim());
        existing.setOptionD(dto.getOptionD().trim());
        existing.setCorrectAnswer(dto.getCorrectAnswer().trim().toUpperCase());
        if (dto.getMarks() != null && dto.getMarks() > 0) {
            existing.setMarks(dto.getMarks());
        }

        Question saved = questionRepository.save(existing);
        return new QuestionAdminDto(
                saved.getId(),
                saved.getQuiz().getId(),
                saved.getQuestion(),
                saved.getOptionA(),
                saved.getOptionB(),
                saved.getOptionC(),
                saved.getOptionD(),
                saved.getCorrectAnswer(),
                saved.getMarks()
        );
    }

    @Transactional
    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question not found with id: " + id);
        }
        questionRepository.deleteById(id);
    }
}
