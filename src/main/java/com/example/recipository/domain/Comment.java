package com.example.recipository.domain;

import com.example.recipository.dto.CommentDto;
import com.example.recipository.dto.CommentResponseDto;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private Recipe recipe;
    private Long groupId;
    private boolean beDeleted;

    public CommentDto.CommentResponseDto toDto(){
        // 댓글을 return 할 String을 만들어두고
        String comment = "";
        // 삭제되었다면 삭제 정보를 넣고
        if(this.beDeleted){
            comment = "삭제된 댓글입니다.";
        // 아니라면 그대로 return
        } else {
            comment = this.comment;
        }

        return CommentDto.CommentResponseDto.builder()
                .commentId(this.commentId)
                .writer(this.writer)
                .comment(comment)
                .groupId(this.groupId)
                .regDate(super.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .beDeleted(this.beDeleted)
                .build();
    }

    public void updateComment(boolean beDeleted){
        this.beDeleted = beDeleted;
    }

    public void updateWriter(String writer){
        this.writer = writer;
    }
}
