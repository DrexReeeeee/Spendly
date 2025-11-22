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

    @Override
    public Category addCategory(String name, User user) {
        Category c = new Category(name, user);
        return categoryRepository.save(c);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category updateCategory(Long id, String name, User user) {
        Optional<Category> opt = categoryRepository.findById(id);
        if (opt.isPresent()) {
            Category c = opt.get();
            c.setName(name);
            c.setUser(user); // ensure ownership stays
            return categoryRepository.save(c);
        }
        return null;
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
