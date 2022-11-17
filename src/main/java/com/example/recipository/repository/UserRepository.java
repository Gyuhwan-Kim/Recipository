package com.example.recipository.repository;

import com.example.recipository.domain.SpUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SpUser, Long> {
    public boolean existsByEmail(String email);
    public boolean existsByName(String name);
    public SpUser save(SpUser user);
    public Optional<SpUser> findSpUserByEmail(String email);
}
