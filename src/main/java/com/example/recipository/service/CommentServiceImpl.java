package com.example.recipository.service;

import com.example.recipository.domain.Comment;
import com.example.recipository.dto.CommentDto;
import com.example.recipository.dto.CommentRequestDto;
import com.example.recipository.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // 댓글을 추가하는 service logic
    @Override
    public Map<String, Object> addComment(CommentDto.CommentRequestDto commentDto, String writer) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Long id = commentRepository.getSequenceValue();
//            commentDto.setCommentId(id);
//            commentDto.setWriter(writer);
            // Dto의 groupId가 null 인 경우는 글에 대한 댓글
            // 따라서 댓글의 id를 groupId 에도 넣어줌
            if (commentDto.getGroupId() == null) {
                commentDto.setGroupId(id);
            }
            Comment comment = commentDto.toEntity(id, writer);

            // Comment entity에 담긴 data를 repository에 save하고
            commentRepository.save(comment);

            // 성공 여부와 comment data가 담긴 Dto를 map에 담아 controller로 전달
            map.put("beAdded", true);
            map.put("commentDto", commentDto);

            return map;
        } catch (Exception e) {
            // 에러로 인한 실패 시에는 실패 정보만을 담아 전달
            map.put("beAdded", false);

            return map;
        }
    }
}
