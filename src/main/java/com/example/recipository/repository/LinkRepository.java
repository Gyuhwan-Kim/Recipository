package com.example.recipository.repository;

import com.example.recipository.domain.Link;
import com.example.recipository.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinkRepository extends JpaRepository<Link, Long> {
    List<Link> findAllByRecipe(Recipe recipe);
}
