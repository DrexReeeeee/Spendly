package com.example.spendly.controller;

import com.example.spendly.model.Category;
import com.example.spendly.model.User;
import com.example.spendly.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //Gets the list of categories for the logged-in user
    @GetMapping("/list")
    public List<Category> listCategories(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return categoryService.getCategories(user);
        }
        return List.of();
    }
    // Adds a new category for the logged-in user
    @PostMapping("/add")
    public Category addCategory(@RequestParam String name, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return null;

        try {
            return categoryService.addCategory(name, user);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Updates an existing category for the logged-in user
    @PostMapping("/update/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestParam String name, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return null;

        try {
            return categoryService.updateCategory(id, name, user);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Deletes a category by its ID for the logged-in user
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return null;

        categoryService.deleteCategory(id);
        return "Success";
    }

    // Gets a single category by its ID
    @GetMapping("/{id}")
    public Category getOne(@PathVariable Long id) {
        Optional<Category> opt = categoryService.findById(id);
        return opt.orElse(null);
    }
}
