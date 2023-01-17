package com.example.recipository.dto;

import com.example.recipository.domain.Comment;
import com.example.recipository.domain.Recipe;
import com.example.recipository.domain.SpUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {
    private String comment;
    private Long targetId;
    private Long groupId;

    public Comment toEntity(Long id, SpUser spUser){
        Recipe recipe = Recipe.builder()
                .contentId(targetId)
                .build();

        return Comment.builder()
                .commentId(id)
                .spUser(spUser)
                .comment(this.comment)
                .recipe(recipe)
                .groupId(this.groupId)
                .build();
    }
}
