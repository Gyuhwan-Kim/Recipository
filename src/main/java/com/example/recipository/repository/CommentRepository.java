package com.example.recipository.repository;

import com.example.recipository.domain.Comment;
import com.example.recipository.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select nextval('comment') from dual", nativeQuery = true)
    Long getSequenceValue();

    List<Comment> findAllByRecipeOrderByGroupIdAscCommentIdAsc(Recipe recipe);

    Comment getCommentByCommentId(Long commentId);

    List<Comment> getAllByWriter(String writer);
}
