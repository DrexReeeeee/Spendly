package com.example.spendly.repository;

import com.example.spendly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository interface for User entity
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
