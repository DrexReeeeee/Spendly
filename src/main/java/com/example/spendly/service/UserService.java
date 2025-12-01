package com.example.spendly.service;

import com.example.spendly.model.User;

// Service interface for User entity
public interface UserService {
    User register(User user);
    User login(String email, String password);
}
