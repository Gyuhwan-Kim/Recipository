package com.example.recipository.repository;

import com.example.recipository.domain.Member;
import com.example.recipository.domain.Recipe;
import com.example.recipository.domain.SpUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query("select r from Recipe r join fetch r.member")
    List<Recipe> getAllRecipe();

    @Query(value = "select r from Recipe r join fetch r.member",
            countQuery = "select count(r) from Recipe r")
    Page<Recipe> getAllWithPagination(Pageable pageable);

    @Query(value = "select name from recipe r " +
            "join member m on r.user_id = m.user_id " +
            "where r.content_id = :id", nativeQuery = true)
    String getWriterForRecipe(@Param("id") Long contentId);

    @Query("select r from Recipe r join fetch r.member where r.contentId = :id")
    Recipe getRecipeByContentId(@Param("id") Long contentId);

    @Query("select r from Recipe r where r.contentId in :ids")
    List<Recipe> getRecipeByContentIds(@Param("ids") Collection<Long> ids);

//    @Query(value = "select * from recipe r join member m on r.user_id = m.user_id where r.user_id = :userId",
//            nativeQuery = true)
//    List<Recipe> getAllByMember(@Param("userId") Long userId);
//    @Query("select r from Recipe r join r.member m where m.userId = :id")
//    List<Recipe> getAllByUserId(@Param("id") Long id);

    @Query("select r from Recipe r join fetch r.member m where m = :user")
    List<Recipe> getAllByMember(@Param("user") Member member);

    @Query(value = "select r from Recipe r join fetch r.member m where m = :user",
            countQuery = "select count(r) from Recipe r where r.member = :user")
    Page<Recipe> getAllByMemberWithPagination(@Param("user") Member member, Pageable pageable);

    @Modifying
    @Query("delete from Recipe r where r.member = :user")
    int deleteAllByMember(@Param("user") Member member);

//    @Modifying
//    @Query(value = "delete from recipe r where r.content_id in :ids", nativeQuery = true)
//    void deleteAllByIds(@Param("ids") Collection<Long> ids);

    @Modifying
    @Query("delete from Recipe r where r.contentId in :ids")
    int deleteAllByIds(@Param("ids") Collection<Long> ids);

    @Modifying
    @Query("delete from Recipe r where r.contentId = :id")
    int deleteRecipeById(@Param("id") Long ids);
}
