package com.example.recipository.repository;

import com.example.recipository.domain.SpUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SpUser, Long> {
    boolean existsByEmail(String email);
    boolean existsByName(String name);
    Optional<SpUser> findSpUserByEmail(String email);
    SpUser getSpUserByEmail(String email);
}
