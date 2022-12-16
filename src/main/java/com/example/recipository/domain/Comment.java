package com.example.recipository.domain;

import com.example.recipository.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
@Entity
@Builder
public class Comment extends BaseTime {
    @Id
    private Long commentId;
    private String writer;
    private String comment;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "target_id")
    private Recipe recipe;
    private Long groupId;

    public CommentDto toDto(){
        return CommentDto.builder()
                .commentId(this.commentId)
                .writer(this.writer)
                .comment(this.comment)
                .groupId(this.groupId)
                .regDate(super.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
