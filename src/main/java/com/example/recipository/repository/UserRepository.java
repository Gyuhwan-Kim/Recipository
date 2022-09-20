package com.example.recipository.repository;

import com.example.recipository.model.entity.UserT;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserT, Long> {
}
