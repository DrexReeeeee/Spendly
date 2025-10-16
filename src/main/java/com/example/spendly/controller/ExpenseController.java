package com.example.spendly.controller;

import com.example.spendly.model.Expense;
import com.example.spendly.model.User;
import com.example.spendly.service.ExpenseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller // Use @Controller for Thymeleaf pages
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // ===============================
    // 1. Thymeleaf page: All expenses
    // ===============================
    @GetMapping("")
    public String expensesPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if(user == null) {
            return "redirect:/login"; // redirect if not logged in
        }

        List<Expense> expenses = expenseService.getExpenses(user);
        model.addAttribute("userExpenses", expenses);

        return "all-expenses"; // Thymeleaf template: all-expenses.html
    }

    // ===============================
    // 2. REST endpoint: List expenses JSON
    // ===============================
    @GetMapping("/list")
    @ResponseBody
    public List<Expense> listExpenses(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user != null) {
            return expenseService.getExpenses(user);
        }
        return new ArrayList<>();
    }

    // ===============================
    // 3. Add new expense (REST)
    // ===============================
    @PostMapping("/add")
    @ResponseBody
    public Expense addExpense(@RequestParam String title,
                              @RequestParam double amount,
                              @RequestParam String category,
                              HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user != null) {
            Expense expense = new Expense(title, amount, LocalDate.now(), category, user);
            return expenseService.addExpense(expense);
        }
        return null;
    }

    // ===============================
    // 4. Update an existing expense (REST)
    // ===============================
    @PostMapping("/update/{id}")
    @ResponseBody
    public Expense updateExpense(@PathVariable Long id,
                                 @RequestParam String title,
                                 @RequestParam double amount,
                                 @RequestParam String category,
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
                expense.setCategory(category);
                return expenseService.updateExpense(expense);
            }
        }
        return null;
    }

    // ===============================
    // 5. Delete an expense (REST)
    // ===============================
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
