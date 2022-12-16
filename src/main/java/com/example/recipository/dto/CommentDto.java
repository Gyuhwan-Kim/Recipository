package com.example.recipository.dto;

import com.example.recipository.domain.Comment;
import com.example.recipository.domain.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long commentId;
    private String writer;
    private String comment;
    private Long targetId;
    private Long groupId;
    private String regDate;

    public Comment toEntity(){
        Recipe recipe = Recipe.builder()
                .contentId(targetId)
                .build();

        return Comment.builder()
                .commentId(this.commentId)
                .writer(this.writer)
                .comment(this.comment)
                .recipe(recipe)
                .groupId(this.groupId)
                .build();
    }
}
