package com.example.recipository.dto;

import com.example.recipository.domain.Comment;
import com.example.recipository.domain.Member;
import com.example.recipository.domain.Recipe;
import com.example.recipository.domain.SpUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CommentDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CommentRequestDto {
        private String comment;
        private Long targetId;
        private Long groupId;

        public Comment toEntity(Long id, Member member){
            Recipe recipe = Recipe.builder()
                    .contentId(targetId)
                    .build();

            return Comment.builder()
                    .commentId(id)
                    .member(member)
                    .comment(this.comment)
                    .recipe(recipe)
                    .groupId(this.groupId)
                    .build();
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CommentResponseDto {
        private Long commentId;
        private String writer;
        private String comment;
        private Long groupId;
        private String regDate;
        private boolean beDeleted;
    }
}
