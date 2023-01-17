package com.example.recipository.service;

import com.example.recipository.domain.SpUser;
import com.example.recipository.dto.CommentDto;
import com.example.recipository.dto.CommentRequestDto;

import java.util.Map;

public interface CommentService {
    Map<String, Object> addComment(CommentDto.CommentRequestDto commentDto, SpUser spUser);
    boolean delComment(Long commentId);
}
