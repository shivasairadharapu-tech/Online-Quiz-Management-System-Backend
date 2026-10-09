package com.example.quiz.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="results")
public class Result {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) @JoinColumn(name="user_id") private User user;
    @ManyToOne(optional=false) @JoinColumn(name="quiz_id") private Quiz quiz;
    private Integer score; private Integer total; private LocalDateTime attemptedAt;
    public Result(){}
    public Result(User u,Quiz q,Integer score,Integer total){this.user=u;this.quiz=q;this.score=score;this.total=total;this.attemptedAt=LocalDateTime.now();}
    public Long getId(){return id;} public User getUser(){return user;} public Quiz getQuiz(){return quiz;} public Integer getScore(){return score;} public Integer getTotal(){return total;} public LocalDateTime getAttemptedAt(){return attemptedAt;}
}
