package com.example.recipository.controller;

import com.example.recipository.domain.SpUser;
import com.example.recipository.dto.CommentDto;
import com.example.recipository.dto.CommentRequestDto;
import com.example.recipository.service.CommentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CommentController {
    private final CommentServiceImpl commentService;

    public CommentController(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    // 댓글 작성 통합
    @PostMapping("/user/add-comment")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Object> addComment(CommentDto.CommentRequestDto commentDto,
                                             @AuthenticationPrincipal SpUser spUser){

        // Authentication principal인 UserDetails 에서 사용자 정보를 받아옴
        String writer = spUser.getUsername();

        // 댓글을 추가하는 service logic을 통과한 후 ResponseEntity로 return
        Map<String, Object> map = commentService.addComment(commentDto, writer);

        return ResponseEntity.ok().body(map);
    }
}
