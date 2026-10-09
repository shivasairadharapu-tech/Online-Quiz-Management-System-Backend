package com.example.quiz.controller;
import com.example.quiz.model.*; import com.example.quiz.repository.*; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/quizzes") @CrossOrigin(origins="http://localhost:5173")
public class QuizController{
 private final QuizRepository quizzes; private final QuestionRepository questions; public QuizController(QuizRepository q,QuestionRepository x){quizzes=q;questions=x;}
 @GetMapping public List<Quiz> all(){return quizzes.findAll();}
 @GetMapping("/{id}") public ResponseEntity<?> one(@PathVariable Long id){return quizzes.findById(id).map(q->ResponseEntity.ok(q)).orElse(ResponseEntity.notFound().build());}
 @GetMapping("/{id}/questions") public ResponseEntity<?> qs(@PathVariable Long id){ if(!quizzes.existsById(id)) return ResponseEntity.notFound().build(); return ResponseEntity.ok(questions.findByQuizId(id).stream().map(x->Map.of("id",x.getId(),"question",x.getQuestion(),"optionA",x.getOptionA(),"optionB",x.getOptionB(),"optionC",x.getOptionC(),"optionD",x.getOptionD())).toList()); }
 @PostMapping public Quiz create(@RequestBody Quiz q){return quizzes.save(q);}
 @PutMapping("/{id}") public ResponseEntity<?> update(@PathVariable Long id,@RequestBody Quiz data){return quizzes.findById(id).map(q->{q.setTitle(data.getTitle());q.setDescription(data.getDescription());q.setCategory(data.getCategory());q.setDifficulty(data.getDifficulty());q.setDurationMinutes(data.getDurationMinutes());return ResponseEntity.ok(quizzes.save(q));}).orElse(ResponseEntity.notFound().build());}
 @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable Long id){if(!quizzes.existsById(id)) return ResponseEntity.notFound().build(); questions.findByQuizId(id).forEach(questions::delete); quizzes.deleteById(id); return ResponseEntity.noContent().build();}
 @GetMapping("/{id}/questions/admin") public ResponseEntity<?> adminQs(@PathVariable Long id){ if(!quizzes.existsById(id)) return ResponseEntity.notFound().build(); return ResponseEntity.ok(questions.findByQuizId(id)); }
 @PostMapping("/{quizId}/questions") public ResponseEntity<?> add(@PathVariable Long quizId,@RequestBody Question data){return quizzes.findById(quizId).map(q->{data.setId(null);data.setQuiz(q);return ResponseEntity.ok(questions.save(data));}).orElse(ResponseEntity.notFound().build());}
 @PutMapping("/questions/{id}") public ResponseEntity<?> updateQ(@PathVariable Long id,@RequestBody Question data){return questions.findById(id).map(q->{q.setQuestion(data.getQuestion());q.setOptionA(data.getOptionA());q.setOptionB(data.getOptionB());q.setOptionC(data.getOptionC());q.setOptionD(data.getOptionD());q.setCorrectAnswer(data.getCorrectAnswer());return ResponseEntity.ok(questions.save(q));}).orElse(ResponseEntity.notFound().build());}
 @DeleteMapping("/questions/{id}") public ResponseEntity<?> deleteQ(@PathVariable Long id){if(!questions.existsById(id)) return ResponseEntity.notFound().build();questions.deleteById(id);return ResponseEntity.noContent().build();}
}
