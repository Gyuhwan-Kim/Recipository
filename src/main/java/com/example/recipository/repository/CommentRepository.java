package com.example.recipository.repository;

import com.example.recipository.domain.Comment;
import com.example.recipository.domain.Member;
import com.example.recipository.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select nextval('comment') from dual", nativeQuery = true)
    Long getSequenceValue();

    List<Comment> findAllByRecipeOrderByGroupIdAscCommentIdAsc(Recipe recipe);

    @Query(value = "select name from comment c " +
            "join member m on c.user_id = m.user_id " +
            "where c.comment_id = :id", nativeQuery = true)
    String getWriterForComment(@Param("id") Long commentId);

    @Query("select c from Comment c join fetch c.member where c.recipe = :recipe " +
            "order by c.groupId asc, c.commentId asc")
    List<Comment> getCommentByRecipe(@Param("recipe") Recipe recipe);

    @Modifying
    @Query("update Comment c " +
            "set c.beDeleted = true, c.member = :newId " +
            "where c.member = :id and c.recipe not in :recipes")
    int updateAllByMember(@Param("id") Member member, @Param("newId") Member deleteMember,
                          @Param("recipes") Collection<Recipe> recipeList);

//    @Modifying
//    @Query(value = "delete from comment c where c.target_id in :ids", nativeQuery = true)
//    void deleteAllByRecipes2(@Param("ids") Collection<Long> ids);

    @Modifying
    @Query("delete from Comment c where c.recipe in :recipes")
    int deleteAllByRecipes(@Param("recipes") Collection<Recipe> recipeList);

    @Modifying
    @Query("delete from Comment c where c.recipe = :recipe")
    int deleteAllByRecipe(@Param("recipe") Recipe recipe);
}
