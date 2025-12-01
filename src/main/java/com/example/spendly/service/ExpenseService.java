package com.example.spendly.service;

import com.example.spendly.model.Expense;
import com.example.spendly.model.User;

import java.util.List;

// Service interface for Expense entity
public interface ExpenseService {
    List<Expense> getExpenses(User user);

    Expense addExpense(Expense expense);

    Expense updateExpense(Expense expense);

    void deleteExpense(Long id);
}
