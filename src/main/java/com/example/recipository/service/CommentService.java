package com.example.recipository.service;

import com.example.recipository.dto.CommentDto;

import java.util.Map;

public interface CommentService {
    Map<String, Object> addComment(CommentDto commentDto, String writer);
}
