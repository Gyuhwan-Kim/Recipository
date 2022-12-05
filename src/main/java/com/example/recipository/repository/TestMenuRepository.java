package com.example.recipository.repository;

import com.example.recipository.model.entity.TestMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestMenuRepository extends JpaRepository<TestMenu, Long> {
    public TestMenu findTestMenuByContentId(Long contentId);
}
