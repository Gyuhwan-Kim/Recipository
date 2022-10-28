package com.example.recipository.repository;

import com.example.recipository.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public boolean existsByEmail(String email);
    public boolean existsByName(String name);
    public User save(User user);
    public User getByEmail(String email);
}
