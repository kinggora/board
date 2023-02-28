package com.example.board.mapper;

import com.example.board.web.model.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface CommentMapper {

    /**
     * 코멘트 저장
     * @param dto 코멘트 정보
     */
    void saveComment(Comment dto);

    /**
     * 해당 게시글에 작성된 코멘트 조회
     * @param postId 게시글 id
     * @return 코멘트 리스트
     */
    ArrayList<Comment> findComment(int postId);
}
