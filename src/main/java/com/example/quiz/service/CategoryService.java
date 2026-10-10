package com.example.quiz.service;

import com.example.quiz.dto.CategoryDto;
import com.example.quiz.exception.BadRequestException;
import com.example.quiz.exception.ResourceNotFoundException;
import com.example.quiz.model.Category;
import com.example.quiz.repository.CategoryRepository;
import com.example.quiz.repository.QuizRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final QuizRepository quizRepository;

    public CategoryService(CategoryRepository categoryRepository, QuizRepository quizRepository) {
        this.categoryRepository = categoryRepository;
        this.quizRepository = quizRepository;
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(c -> {
            long count = quizRepository.findAll().stream()
                    .filter(q -> c.getName().equalsIgnoreCase(q.getCategory()) && Boolean.TRUE.equals(q.getPublished()))
                    .count();
            return new CategoryDto(c.getId(), c.getName(), c.getDescription(), c.getIcon(), count);
        }).collect(Collectors.toList());
    }

    @Transactional
    public CategoryDto createCategory(Category category) {
        if (categoryRepository.existsByNameIgnoreCase(category.getName().trim())) {
            throw new BadRequestException("Category with name '" + category.getName() + "' already exists");
        }
        category.setName(category.getName().trim());
        Category saved = categoryRepository.save(category);
        return new CategoryDto(saved.getId(), saved.getName(), saved.getDescription(), saved.getIcon(), 0);
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
