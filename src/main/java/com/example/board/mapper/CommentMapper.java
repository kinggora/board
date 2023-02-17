package com.example.board.mapper;

import com.example.board.web.model.CommentDto;

import java.util.ArrayList;

public interface CommentMapper {
    void saveComment(CommentDto dto);
    ArrayList<CommentDto> findComment(int postId);
}
