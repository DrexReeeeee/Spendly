package com.example.spendly.service.impl;

import com.example.spendly.model.User;
import com.example.spendly.repository.UserRepository;
import com.example.spendly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    // Registers a new user
    @Override
    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists!");
        }
        return userRepository.save(user);
    }

    // Logs in a user with the given email and password
    @Override
    public User login(String email, String password) {
        if (email == null || password == null) return null;

        User user = userRepository.findByEmail(email);
        if (user != null && password.equals(user.getPassword())) {
            return user;
        }
        return null;
    }
}
