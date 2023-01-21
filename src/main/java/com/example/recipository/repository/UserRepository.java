package com.example.recipository.repository;

import com.example.recipository.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    boolean existsByName(String name);
    Optional<Member> findMemberByEmail(String email);
    Member getMemberByEmail(String email);
}
