package com.example.spendly.service.impl;

import com.example.spendly.model.Category;
import com.example.spendly.model.User;
import com.example.spendly.repository.CategoryRepository;
import com.example.spendly.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getCategories(User user) {
        return categoryRepository.findByUserId(user.getId());
    }
    // Adds a new category for the specified user
    @Override
    public Category addCategory(String name, User user) {

        String normalized = name.trim();

        // Check duplicate
        Optional<Category> existing =
                categoryRepository.findByNameIgnoreCaseAndUserId(normalized, user.getId());

        if (existing.isPresent()) {
            throw new RuntimeException("Category already exists.");
        }

        Category c = new Category(normalized, user);
        return categoryRepository.save(c);
    }
    // Retrieves a category by its ID
    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    // Updates an existing category for the specified user
    @Override
    public Category updateCategory(Long id, String name, User user) {

        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found."));

        String normalized = name.trim();

        // Check if another category with the same name exists
        Optional<Category> duplicate =
                categoryRepository.findByNameIgnoreCaseAndUserId(normalized, user.getId());

        if (duplicate.isPresent() && !duplicate.get().getId().equals(id)) {
            throw new RuntimeException("Category name already exists.");
        }

        c.setName(normalized);
        c.setUser(user);
        return categoryRepository.save(c);
    }

    // Deletes a category by its ID
    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
