package com.example.spendly.controller;

import com.example.spendly.model.Category;
import com.example.spendly.model.User;
import com.example.spendly.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public List<Category> listCategories(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return categoryService.getCategories(user);
        }
        return List.of();
    }

    @PostMapping("/add")
    public Category addCategory(@RequestParam String name, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return categoryService.addCategory(name, user);
        }
        return null;
    }

    @PostMapping("/update/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestParam String name, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return categoryService.updateCategory(id, name, user);
        }
        return null;
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        categoryService.deleteCategory(id);
        return "Success";
    }

    @GetMapping("/{id}")
    public Category getOne(@PathVariable Long id) {
        Optional<Category> opt = categoryService.findById(id);
        return opt.orElse(null);
    }
}
