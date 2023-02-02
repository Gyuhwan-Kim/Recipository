package com.example.recipository.repository;

import com.example.recipository.domain.Comment;
import com.example.recipository.domain.Member;
import com.example.recipository.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select nextval('comment') from dual", nativeQuery = true)
    Long getSequenceValue();

    List<Comment> findAllByRecipeOrderByGroupIdAscCommentIdAsc(Recipe recipe);

    Comment getCommentByCommentId(Long commentId);

    @Query("select c from Comment c join fetch c.member where c.recipe = :recipe")
    List<Comment> getCommentByRecipe(@Param("recipe") Recipe recipe);

    @Modifying
    @Query("update Comment c set c.beDeleted = true, c.member = :newId where c.member = :id")
    int updateAllByMember(@Param("id") Member member, @Param("newId") Member deleteMember);
}
