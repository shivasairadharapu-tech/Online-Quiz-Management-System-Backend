package com.example.quiz.repository;
import com.example.quiz.model.Result; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface ResultRepository extends JpaRepository<Result,Long>{ List<Result> findByUserIdOrderByAttemptedAtDesc(Long userId); }
