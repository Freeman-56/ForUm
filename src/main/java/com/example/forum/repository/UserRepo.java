package com.example.forum.repository;

import com.example.forum.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByOauth2Name(String username);
}
