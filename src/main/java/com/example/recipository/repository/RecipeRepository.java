package com.example.recipository.repository;

import com.example.recipository.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Recipe getRecipeByContentId(Long contentId);
}
