package com.example.spendly.service;

import com.example.spendly.model.Category;
import com.example.spendly.model.User;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getCategories(User user);
    Category addCategory(String name, User user);
    Optional<Category> findById(Long id);
    Category updateCategory(Long id, String name, User user);
    void deleteCategory(Long id);
}
