package com.example.recipository.repository;

import com.example.recipository.domain.Link;
import com.example.recipository.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface LinkRepository extends JpaRepository<Link, Long> {
    List<Link> findAllByRecipe(Recipe recipe);

    @Modifying
    @Query("delete from Link l where l.recipe in :recipes")
    int deleteAllByRecipes(@Param("recipes") Collection<Recipe> recipeList);
}
