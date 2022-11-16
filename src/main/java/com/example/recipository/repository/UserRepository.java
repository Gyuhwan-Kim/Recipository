package com.example.recipository.repository;

import com.example.recipository.domain.SpUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SpUser, Long> {
    public boolean existsByEmail(String email);
    public boolean existsByName(String name);
    public SpUser save(SpUser user);
    public SpUser getByEmail(String email);
}
