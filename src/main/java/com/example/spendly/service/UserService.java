package com.example.spendly.service;

import com.example.spendly.model.User;

public interface UserService {
    User register(User user);
    User login(String email, String password);
}
