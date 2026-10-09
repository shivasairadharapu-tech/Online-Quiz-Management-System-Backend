package com.example.quiz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name="questions")
public class Question {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="quiz_id") @JsonIgnore private Quiz quiz;
    @Column(nullable=false, length=1000) private String question;
    @Column(nullable=false) private String optionA;
    @Column(nullable=false) private String optionB;
    @Column(nullable=false) private String optionC;
    @Column(nullable=false) private String optionD;
    @Column(nullable=false) private String correctAnswer;

    public Question() {}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Quiz getQuiz(){return quiz;} public void setQuiz(Quiz quiz){this.quiz=quiz;}
    public String getQuestion(){return question;} public void setQuestion(String question){this.question=question;}
    public String getOptionA(){return optionA;} public void setOptionA(String v){this.optionA=v;}
    public String getOptionB(){return optionB;} public void setOptionB(String v){this.optionB=v;}
    public String getOptionC(){return optionC;} public void setOptionC(String v){this.optionC=v;}
    public String getOptionD(){return optionD;} public void setOptionD(String v){this.optionD=v;}
    public String getCorrectAnswer(){return correctAnswer;} public void setCorrectAnswer(String v){this.correctAnswer=v;}
}
