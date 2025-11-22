package com.example.spendly.controller;

import com.example.spendly.model.Category;
import com.example.spendly.model.Expense;
import com.example.spendly.model.User;
import com.example.spendly.service.CategoryService;
import com.example.spendly.service.ExpenseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String expensesPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if(user == null) {
            return "redirect:/login";
        }

        List<Expense> expenses = expenseService.getExpenses(user);
        model.addAttribute("userExpenses", expenses);

        // load categories for initial page render (optional)
        model.addAttribute("categories", categoryService.getCategories(user));

        return "all-expenses";
    }

    @GetMapping("/list")
    @ResponseBody
    public List<Expense> listExpenses(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user != null) {
            return expenseService.getExpenses(user);
        }
        return new ArrayList<>();
    }

    @PostMapping("/add")
    @ResponseBody
    public Expense addExpense(@RequestParam String title,
                              @RequestParam double amount,
                              @RequestParam Long categoryId,
                              HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user != null) {
            Optional<Category> catOpt = categoryService.findById(categoryId);
            Category category = catOpt.orElse(null);
            Expense expense = new Expense(title, amount, LocalDate.now(), category, user);
            return expenseService.addExpense(expense);
        }
        return null;
    }

    @PostMapping("/update/{id}")
    @ResponseBody
    public Expense updateExpense(@PathVariable Long id,
                                 @RequestParam String title,
                                 @RequestParam double amount,
                                 @RequestParam Long categoryId,
                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user != null) {
            Expense expense = expenseService.getExpenses(user).stream()
                    .filter(e -> e.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if(expense != null) {
                expense.setTitle(title);
                expense.setAmount(amount);
                categoryService.findById(categoryId).ifPresent(expense::setCategory);
                return expenseService.updateExpense(expense);
            }
        }
        return null;
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public String deleteExpense(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user != null) {
            expenseService.deleteExpense(id);
            return "Success";
        }
        return "Failed";
    }
}
