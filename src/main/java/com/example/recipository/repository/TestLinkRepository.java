package com.example.recipository.repository;

import com.example.recipository.model.entity.TestLink;
import com.example.recipository.model.entity.TestMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestLinkRepository extends JpaRepository<TestLink, Long> {
    public List<TestLink> findAllByTestMenu(TestMenu testMenu);
}
