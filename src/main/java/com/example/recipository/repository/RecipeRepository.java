package com.example.recipository.repository;

import com.example.recipository.domain.Member;
import com.example.recipository.domain.Recipe;
import com.example.recipository.domain.SpUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query("select r from Recipe r join fetch r.member where r.contentId = :id")
    Recipe getRecipeByContentId(@Param("id") Long contentId);

//    @Query(value = "select * from recipe r join member m on r.user_id = m.user_id where r.user_id = :userId",
//            nativeQuery = true)
//    List<Recipe> getAllByMember(@Param("userId") Long userId);
//    @Query("select r from Recipe r join r.member m where m.userId = :id")
//    List<Recipe> getAllByUserId(@Param("id") Long id);

    @Query("select r from Recipe r join fetch r.member m where m = :user")
    List<Recipe> getAllByMember(@Param("user") Member member);
}
