package com.example.quiz.model;

import jakarta.persistence.*;

@Entity
@Table(name="quizzes")
public class Quiz {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false) private String title;
    private String description;
    private String category;
    private String difficulty;
    private Integer durationMinutes = 10;

    public Quiz() {}
    public Quiz(String title,String description,String category,String difficulty,Integer durationMinutes){this.title=title;this.description=description;this.category=category;this.difficulty=difficulty;this.durationMinutes=durationMinutes;}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getTitle(){return title;} public void setTitle(String title){this.title=title;}
    public String getDescription(){return description;} public void setDescription(String description){this.description=description;}
    public String getCategory(){return category;} public void setCategory(String category){this.category=category;}
    public String getDifficulty(){return difficulty;} public void setDifficulty(String difficulty){this.difficulty=difficulty;}
    public Integer getDurationMinutes(){return durationMinutes;} public void setDurationMinutes(Integer durationMinutes){this.durationMinutes=durationMinutes;}
}
