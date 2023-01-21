package com.example.recipository.repository;

import com.example.recipository.domain.Member;
import com.example.recipository.domain.Recipe;
import com.example.recipository.domain.SpUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Recipe getRecipeByContentId(Long contentId);
    List<Recipe> getAllByMember(Member member);
}
