package com.example.recipository.service;

import com.example.recipository.domain.Member;
import com.example.recipository.domain.SpUser;
import com.example.recipository.dto.CommentDto;
import com.example.recipository.dto.CommentRequestDto;

import java.util.Map;

public interface CommentService {
    Map<String, Object> addComment(CommentDto.CommentRequestDto commentDto, Member member);
    boolean delComment(Long commentId);
}
